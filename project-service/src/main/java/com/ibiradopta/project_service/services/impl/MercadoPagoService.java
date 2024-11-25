package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.ibiradopta.project_service.services.IMercadoPagoService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {

    @Value("${mercadoPago.accessToken}")
    private String accessToken;

    @Override
    public Preference createPreference(List<ProjectDto> projects) throws MPException, MPApiException {

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
                .success("https://pruebakeycloak.vercel.app/success") // URL a la que se redirige en caso de éxito.
                .pending("https://pruebakeycloak.vercel.app/pending") // URL a la que se redirige en caso de que el pago esté pendiente.
                .failure("https://pruebakeycloak.vercel.app/failure") // URL a la que se redirige en caso de fallo.
                .build();

        // Crear la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .autoReturn("approved") // Configurar el retorno automático.
                .build();

//        // Crear cliente para llamar al API de MercadoPago
        PreferenceClient client = new PreferenceClient();
//        return client.create(preferenceRequest);

        // Crear la preferencia de pago usando el cliente de preferencias.
        Preference preference = client.create(preferenceRequest);

        // Retornar el punto de inicio de la preferencia (URL de redirección para
        // iniciar el pago).
        return preference;
    }
}

