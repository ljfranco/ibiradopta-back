package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import java.util.List;

public interface IMercadoPagoService {
    public String createPreference(List<ProjectDto> projects) throws MPException, MPApiException;
}
