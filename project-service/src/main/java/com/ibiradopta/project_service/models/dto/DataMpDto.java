package com.ibiradopta.project_service.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataMpDto {

    @Override
    public String toString() {
        return "DataMp [id=" + id + "]";
    }

    private String id;
}
