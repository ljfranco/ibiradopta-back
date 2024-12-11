package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.dto.*;
import com.ibiradopta.project_service.services.impl.MercadoPagoService;
import com.ibiradopta.project_service.services.impl.PaymentService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
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
    @PreAuthorize("hasRole('ROLE_Administrador')")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(summary = "Get a payment by userId", description = "Obtain an existing payment, the respone is the payment")
    @GetMapping("/userId/{userId}")
    //Autorizar solamente si es el mismo usuario quien consulta o un administrador
    @PreAuthorize("#userId == authentication.name or hasRole('ROLE_Administrador')")
    public ResponseEntity<List<PaymentDto>> getPaymentByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
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
    public ResponseEntity<MPResponseDto> newPreference(@RequestParam String userId, @RequestBody List<ProjectDto> projects) throws MPException, MPApiException {

        MPResponseDto response = new MPResponseDto();
        response.setPreferenceUrl(mercadoPagoService.createPreference(userId, projects));
        // Crear la preferencia con los productos recibidos
        return ResponseEntity.ok(response);

        //return ResponseEntity.ok(preferenceId);
    }
}
