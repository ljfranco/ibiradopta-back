package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.dto.MpNotifyDto;
import com.ibiradopta.project_service.models.dto.PaymentDto;
import com.ibiradopta.project_service.models.dto.PaymentMPDto;
import com.ibiradopta.project_service.models.dto.ProjectDto;
import com.ibiradopta.project_service.services.impl.MercadoPagoService;
import com.ibiradopta.project_service.services.impl.PaymentService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<String> newPreference(@RequestParam String userId, @RequestBody List<ProjectDto> projects) throws MPException, MPApiException {

        // Crear la preferencia con los productos recibidos
        return ResponseEntity.ok(mercadoPagoService.createPreference(userId, projects));

        //return ResponseEntity.ok(preferenceId);
    }

    // Endpoint para recibir notificaciones de MercadoPago
    @PostMapping("/notify")
    public ResponseEntity<String> notifyPay(@RequestBody MpNotifyDto mpNotify) {
        // Log de la notificación para depuración
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Notificación de pago recibida: {}", mpNotify.toString());

        // Usar el ID del pago recibido en la notificación para consultar el estado
        try {
            String paymentId = mpNotify.getData().getId();  // Aquí obtenemos el ID del pago

            List<PaymentMPDto> payments = mercadoPagoService.getPayment(Long.parseLong(paymentId));  // Consultar el estado del pago desde la API de MercadoPago

            // Guardar la información en la base de datos según el estado
            mercadoPagoService.savePayment(payments);

            logger.info("Notificación procesada con éxito");
            return ResponseEntity.ok("Notificación procesada con éxito");
        } catch (Exception e) {
            logger.error("Error al procesar la notificación de pago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la notificación");
        }
}
}
