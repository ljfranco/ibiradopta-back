package com.ibiradopta.project_service.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentMPDto {
    private Long id;
    private Integer quantity;
    private Double amount;
    private LocalDate date;
    private String userId;
    private String userEmail;
    private String userName;
    private String projectId;
    private String projectName;
}
