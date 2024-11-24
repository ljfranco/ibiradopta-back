package com.ibiradopta.project_service.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDto {

    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String location;
    private Integer quantity;
    private BigDecimal unitPrice;



}
