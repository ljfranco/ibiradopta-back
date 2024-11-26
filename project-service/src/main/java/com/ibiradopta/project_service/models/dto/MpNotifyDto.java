package com.ibiradopta.project_service.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class MpNotifyDto {
        private Long id;
        private Boolean liveMode;
        private String type;
        private OffsetDateTime dateCreated;
        private Long userID;
        private String apiVersion;
        private String action;
        private DataMpDto data;
}
