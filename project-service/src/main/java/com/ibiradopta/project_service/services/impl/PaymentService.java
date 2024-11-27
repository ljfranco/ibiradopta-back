package com.ibiradopta.project_service.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibiradopta.project_service.feignClient.UserClient;
import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.models.dto.PaymentDto;
import com.ibiradopta.project_service.models.dto.ProjectDto;
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
                    return new PaymentDto(payment.getId().toString(),payment.getAmount(),payment.getDate(), user,mapper.convertValue(payment.getProject(), ProjectDto.class));
                })
                .collect(Collectors.toList());
    }

    @Override
    public void savePayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setId(Long.valueOf(paymentDto.getId()));
        payment.setAmount(paymentDto.getAmount());
        payment.setDate(paymentDto.getDate());
        payment.setUserId(paymentDto.getUser().getId());
        payment.setProject(mapper.convertValue(paymentDto.getProject(), Project.class));
        paymentRepository.save(payment);
    }

    @Override
    public void saveAllPayments(List<PaymentDto> paymentDtos) {
        List<Payment> payments = paymentDtos.stream()
                .map(paymentDto -> {
                    Payment payment = new Payment();
                    payment.setId(Long.valueOf(paymentDto.getId()));
                    payment.setAmount(paymentDto.getAmount());
                    payment.setDate(paymentDto.getDate());
                    payment.setUserId(paymentDto.getUser().getId());
                    payment.setProject(mapper.convertValue(paymentDto.getProject(), Project.class));
                    return payment;
                })
                .collect(Collectors.toList());
        paymentRepository.saveAll(payments);

    }


}
