package com.ibiradopta.project_service.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetailsDto {
    String recipient;
    String subject;
    String userName;
    String projectName;
    String paymentDate;
    String quantity;
    String amount;
}
