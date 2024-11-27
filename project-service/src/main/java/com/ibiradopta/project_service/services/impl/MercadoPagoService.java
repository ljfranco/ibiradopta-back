package com.ibiradopta.project_service.services.impl;


import com.ibiradopta.project_service.models.dto.MetadataMPDto;
import com.ibiradopta.project_service.models.dto.PaymentMPDto;
import com.ibiradopta.project_service.models.dto.ProjectDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPago.accessToken}")
    private String accessToken;

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
                    .unitPrice(project.getUnitPrice())
                    .build();
            items.add(itemRequest);
        }

        // Crear un objeto 'PreferenceBackUrlsRequest' para definir las URLs de retorno.
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:3000/SuccessPayment") // URL a la que se redirige en caso de éxito.
                .pending("https://pruebakeycloak.vercel.app/pending") // URL a la que se redirige en caso de que el pago esté pendiente.
                .failure("https://pruebakeycloak.vercel.app/failure") // URL a la que se redirige en caso de fallo.
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


        // Crear una lista de objetos PaymentMPDto por cada id de proyecto en los metadatos para luego guardarlos en la base de datos con el estado obtenido

        List<PaymentMPDto> dbPayments = new ArrayList<>();
        List<String> projectIds = new ArrayList<>();
        Object projectIdsObject = paymentFromMP.getMetadata().get("projectIds"); // Obtener el valor del mapa
        if (projectIdsObject instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String) {
                    projectIds.add((String) item);  // Agregar solo si el elemento es una cadena
                }
            }
        } else {
            // Manejar el caso donde 'projectIds' no es una lista
            System.out.println("El valor de projectIds no es una lista válida.");
        }
        for (String projectId : projectIds) {
            PaymentMPDto dbPayment = new PaymentMPDto();
            dbPayment.setProjectId(projectId);
            dbPayment.setId(paymentFromMP.getId());
            dbPayment.setUserId(paymentFromMP.getMetadata().get("userId").toString());
            //convertir bigdecimal a double
            dbPayment.setAmount(paymentFromMP.getTransactionAmount().doubleValue());
            dbPayment.setDate(paymentFromMP.getDateApproved().toLocalDate());
            dbPayments.add(dbPayment);
        }


        return dbPayments;
    }

    //Guardar el pago en la base de datos
    @Override
    public void savePayment(List<PaymentMPDto> payments) {
        // Guardar los pagos en la base de datos


        //paymentRepository.saveAll(payments);

    }

}

