package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.dto.PaymentDto;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllPayments();

    List<PaymentDto> getPaymentsByFilters(Integer projectId, String UserId, String startDate, String endDate);

    void savePayment(PaymentDto paymentDto);

    void saveAllPayments(List<PaymentDto> paymentDtos);
}
