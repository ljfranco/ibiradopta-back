package com.ibiradopta.project_service.controller;

import com.ibiradopta.project_service.models.Payment;
import com.ibiradopta.project_service.models.Project;
import com.ibiradopta.project_service.models.dto.PaymentDto;
import com.ibiradopta.project_service.models.dto.RequestDto;
import com.ibiradopta.project_service.services.impl.PaymentService;
import com.ibiradopta.project_service.utils.CustomerUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    String STRIPE_API_KEY = System.getenv().get("STRIPE_SECRET_KEY");

    @PostMapping("/checkout/hosted")
    String hostedCheckout(@RequestBody RequestDto requestDTO) throws StripeException {

        Stripe.apiKey = STRIPE_API_KEY;
        String clientBaseURL = System.getenv().get("CLIENT_BASE_URL");

        // Start by finding an existing customer record from Stripe or creating a new one if needed
        Customer customer = CustomerUtil.findOrCreateCustomer(requestDTO.getCustomerEmail(), requestDTO.getCustomerName());

        // Next, create a checkout session by adding the details of the checkout
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setCustomer(customer.getId())
                        .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientBaseURL + "/failure");

        for (Project project : requestDTO.getItems()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .putMetadata("app_id", project.getId().toString())
                                                            .setName(project.getName())
                                                            .build()
                                            )
                                            .setCurrency("UYU")
                                            .setUnitAmountDecimal(BigDecimal.valueOf(project.getAmount()))
                                            .build())
                            .build());
        }


        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }
}
