package com.ibiradopta.project_service.services.impl;


import com.ibiradopta.project_service.models.dto.*;
import com.ibiradopta.project_service.services.IMercadoPagoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPago.accessToken}")
    private String accessToken;

    @Value("${mercadoPago.successUrl}")
    private String successUrl;

    @Value("${mercadoPago.failureUrl}")
    private String failureUrl;

    @Autowired
    private PaymentService paymentService;

    @Override
    public String createPreference(String userId, List<ProjectDto> projects) throws MPException, MPApiException {

        // Configura el token de acceso para MercadoPago con la variable 'accesToken'.
        MercadoPagoConfig.setAccessToken(accessToken);

        List<PreferenceItemRequest> items = new ArrayList<>();

        // Convertir los projectos recibidos a items de la preferencia
        for (ProjectDto project : projects) {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(project.getId())
                    .title(project.getName())
                    .description(project.getDescription())
                    .pictureUrl(project.getImageUrl())
                    .categoryId(project.getLocation())
                    .quantity(project.getQuantity())
                    .currencyId("UYU") // Cambia la moneda según tu necesidad
                    .unitPrice(project.getPrice())
                    .build();
            items.add(itemRequest);
        }

        // Crear un objeto 'PreferenceBackUrlsRequest' para definir las URLs de retorno.
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(successUrl) // URL a la que se redirige en caso de éxito.
                .pending(failureUrl) // URL a la que se redirige en caso de que el pago esté pendiente.
                .failure(failureUrl) // URL a la que se redirige en caso de fallo.
                .build();

        //Crear un objeto 'MetadataMPDto' para agregar metadatos a la preferencia.
        MetadataMPDto metadata = new MetadataMPDto();
        //creo lista con ids de proyectos
        List<String> projectIds = new ArrayList<>();
        for (ProjectDto project : projects) {
            projectIds.add(project.getId());
        }
        metadata.setProjectIds(projectIds);
        metadata.setUserId(userId);


        // Crear la preferencia de Pago
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                //.autoReturn("approved") // Configurar el retorno automático.
                .metadata(metadata.toMap()) // Agregar los metadatos a la preferencia.
                .build();

//        // Crear cliente para llamar al API de MercadoPago
        PreferenceClient client = new PreferenceClient();
//        return client.create(preferenceRequest);

        // Crear la preferencia de pago usando el cliente de preferencias.
        Preference preference = client.create(preferenceRequest);

        // Retornar el punto de inicio de la preferencia (URL de redirección para
        // iniciar el pago).
        return preference.getInitPoint();
    }


    //Obtener el pago de MercadoPago
    @Override
    public List<PaymentMPDto> getPayment(Long paymentId) throws MPException, MPApiException {
        // Configura el token de acceso para MercadoPago
        MercadoPagoConfig.setAccessToken(accessToken);

        // Realizar la consulta del pago usando el ID recibido en la notificación
        PaymentClient paymentClient = new PaymentClient();
        Payment paymentFromMP = paymentClient.get(paymentId);


        // Crear la lista de PaymentMPDto a partir de los items
        List<PaymentMPDto> dbPayments = paymentFromMP.getAdditionalInfo()
                .getItems()
                .stream()
                .map(item -> {
                    PaymentMPDto dbPayment = new PaymentMPDto();
                    dbPayment.setProjectId(item.getId()); // Aquí asumo que el ID del proyecto está en el campo `id` del item
                    dbPayment.setId(paymentFromMP.getId());
                    dbPayment.setQuantity(item.getQuantity());
                    dbPayment.setUserId(paymentFromMP.getMetadata().get("user_id").toString());
                    dbPayment.setAmount(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue());
                    dbPayment.setDate(paymentFromMP.getDateApproved().toLocalDate());
                    return dbPayment;
                })
                .collect(Collectors.toList());

        return dbPayments;
    }

    //Guardar el pago en la base de datos
    @Override
    public void savePayment(List<PaymentMPDto> payments) {
        // Convertir los PaymentMPDto a PaymentDto
        List<PaymentDto> paymentDtos = payments.stream()
                .map(this::convertToPaymentDto)
                .collect(Collectors.toList());

        // Llamar al metodo de PaymentService para guardar los pagos
        paymentService.saveAllPayments(paymentDtos);

    }

    // Metodo para convertir PaymentMPDto a PaymentDto
    private PaymentDto convertToPaymentDto(PaymentMPDto paymentMPDto) {
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setId(String.valueOf(paymentMPDto.getId()));
        paymentDto.setQuantity(paymentMPDto.getQuantity());
        paymentDto.setAmount(paymentMPDto.getAmount());
        paymentDto.setDate(paymentMPDto.getDate());

        // Convertir userId a un UserDto
        UserDto userDto = new UserDto();  // Aquí debes obtener el UserDto correspondiente al userId
        userDto.setId(paymentMPDto.getUserId());
        paymentDto.setUser(userDto);

        // Convertir projectId a un ProjectDto
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(paymentMPDto.getProjectId()); // Asignar el ID del proyecto
        paymentDto.setProject(projectDto);

        return paymentDto;

    }
}

