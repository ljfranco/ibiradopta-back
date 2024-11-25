//package com.ibiradopta.project_service.controller;
//
//import com.ibiradopta.project_service.models.dto.ProjectDto;
//import com.ibiradopta.project_service.services.impl.MercadoPagoService;
//import com.mercadopago.exceptions.MPApiException;
//import com.mercadopago.exceptions.MPException;
//import com.mercadopago.resources.preference.Preference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/mercadopago")
//public class MercadoPagoController {
//
//    @Autowired
//    private MercadoPagoService mercadoPagoService;
//
//    // Endpoint para crear preferencia
//    @PostMapping("/createpreference")
//    public Preference newPreference(@RequestBody List<ProjectDto> projects) throws MPException, MPApiException {
//
//        // Crear la preferencia con los productos recibidos
//        return mercadoPagoService.createPreference(projects);
//    }
//}
