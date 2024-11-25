package com.ibiradopta.project_service.models.dto;

import com.ibiradopta.project_service.models.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestDto {
    Project[] items;
    String customerName;
    String customerEmail;

}
