package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.ibiradopta.project_service.services.IMercadoPagoService;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService implements IMercadoPagoService {
    @Override
    public Preference createPreference(List<ProjectDto> projects) throws MPException, MPApiException {

        List<PreferenceItemRequest> items = new ArrayList<>();

        // Convertir los projectos recibidos a items de la preferencia
        for (ProjectDto project : projects) {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(project.getId()))
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

        // Crear la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        // Crear cliente para llamar al API de MercadoPago
        PreferenceClient client = new PreferenceClient();
        return client.create(preferenceRequest);
    }
}

