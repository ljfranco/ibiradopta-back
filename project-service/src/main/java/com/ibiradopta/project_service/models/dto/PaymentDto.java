package com.ibiradopta.project_service.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    // Payment Data
    private String id;
    private Double amount;
    private LocalDate date;
    // User Data
    private UserDto user;
    //Project Data
    private ProjectDto project;
}
