package com.ibiradopta.project_service.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibiradopta.project_service.feignClient.UserClient;
import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.dto.PaymentDto;
import com.ibiradopta.project_service.models.dto.UserDto;
import com.ibiradopta.project_service.repositories.IPaymentRepository;
import com.ibiradopta.project_service.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    ObjectMapper mapper;


    @Override
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    @Override
    public List<PaymentDto> getPaymentsByFilters(Integer projectId, String UserId, String startDate, String endDate) {
        List<Payment> payments = paymentRepository.findByFilters(projectId, UserId, startDate, endDate);

        return payments.stream()
                .map(payment -> {
                    //Obtener el usuario relacionado al pago
                    System.out.println("payment.getUserId()"+payment.getUserId());
                    UserDto user = userClient.getUserById(payment.getUserId());
                    return new PaymentDto(payment.getId(),payment.getAmount(),payment.getDate(), user,payment.getProject());
                })
                .collect(Collectors.toList());
    }
}
