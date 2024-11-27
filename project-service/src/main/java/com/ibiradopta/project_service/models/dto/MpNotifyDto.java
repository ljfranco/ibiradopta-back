package com.ibiradopta.project_service.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class MpNotifyDto {
        private Long id;
        private Boolean live_mode;
        private String type;
        private OffsetDateTime date_created;
        private Long user_id;
        private String api_version;
        private String action;
        private DataMpDto data;
}
