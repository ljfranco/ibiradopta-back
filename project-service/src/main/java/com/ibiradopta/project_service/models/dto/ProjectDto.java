package com.ibiradopta.project_service.models.dto;

import com.ibiradopta.project_service.models.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDto {

    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String location;
    private Integer quantity;
    private LocalDate endDate;
    private BigDecimal price;
    private List<Image> images;



}
