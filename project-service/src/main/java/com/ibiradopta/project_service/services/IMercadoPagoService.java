package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.dto.PaymentMPDto;
import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.List;

public interface IMercadoPagoService {
    public String createPreference(String userId, List<ProjectDto> projects) throws MPException, MPApiException;
    public List<PaymentMPDto> getPayment(Long paimentId) throws MPException, MPApiException;
    public void savePayment(List<PaymentMPDto> payments);
}
