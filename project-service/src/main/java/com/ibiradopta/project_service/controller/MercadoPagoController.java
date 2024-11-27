package com.ibiradopta.project_service.controller;


import com.ibiradopta.project_service.models.dto.MpNotifyDto;
import com.ibiradopta.project_service.models.dto.PaymentMPDto;
import com.ibiradopta.project_service.services.impl.MercadoPagoService;
import com.mercadopago.net.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mercadopago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

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
