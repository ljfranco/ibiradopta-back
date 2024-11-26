package com.ibiradopta.project_service.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataMPDto {
    private List<String> projectIds;
    private String userId;

//     Metodo para convertir MetadataMPDto a Map<String, Object>
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("projectIds", this.projectIds);
        map.put("userId", this.userId);
        return map;
    }
}
