package com.amr.project.webapp.controller;

import com.amr.project.model.entity.Order;
import com.amr.project.service.abstracts.OrderService;
import com.amr.project.webapp.handler.PaymentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentApi paymentApi;
    private final OrderService orderService;
    @Autowired
    public PaymentController(PaymentApi paymentApi,
                             OrderService orderService) {
        this.paymentApi = paymentApi;
        this.orderService = orderService;
    }
    @GetMapping("/{id}")
    public String getPayment(@PathVariable Long id) {
        Order order = orderService.getByKey(id);
//        return "redirect: " + paymentApi.payUrl(order);
        return "redirect:"+ paymentApi.payUrl(order);
    }
}
