package com.ibiradopta.project_service.services;

import com.ibiradopta.project_service.models.dto.EmailDetailsDto;

public interface IEmailService {
    public void sendEmail( EmailDetailsDto emailDetails);
}
