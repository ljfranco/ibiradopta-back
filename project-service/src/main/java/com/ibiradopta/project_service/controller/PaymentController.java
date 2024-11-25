package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.dto.PaymentDto;
import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.ibiradopta.project_service.services.impl.MercadoPagoService;
import com.ibiradopta.project_service.services.impl.PaymentService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Get all payments", description = "Obtain all existing payments, the respone is a list of payments")
    @GetMapping("/getall")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(summary = "Get payments by filters", description = "Obtain existing payments by filters, the respone is a list of payments")
    @GetMapping("/filters")
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<List<PaymentDto>> getPaymentsByFilters(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(paymentService.getPaymentsByFilters(projectId, userId, startDate, endDate));
    }
    @Autowired
    private MercadoPagoService mercadoPagoService;

    // Endpoint para crear preferencia
    @PostMapping("/createpreference")
    public ResponseEntity<Preference> newPreference(@RequestBody List<ProjectDto> projects) throws MPException, MPApiException {

        // Crear la preferencia con los productos recibidos
        return ResponseEntity.ok(mercadoPagoService.createPreference(projects));

        //return ResponseEntity.ok(preferenceId);
    }
}
