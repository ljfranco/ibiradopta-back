package com.ibiradopta.project_service.models.dto;

import com.ibiradopta.project_service.models.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    // Payment Data
    private Integer id;
    private Double amount;
    private LocalDate date;
    // User Data
    private UserDto user;
    //Project Data
    private Project project;
}
