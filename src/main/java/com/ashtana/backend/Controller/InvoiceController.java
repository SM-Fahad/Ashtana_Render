package com.ashtana.backend.Controller;

import com.ashtana.backend.Entity.Order;

import com.ashtana.backend.Service.JasperReportService;
import com.ashtana.backend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long orderId) {
        try {
            System.out.println("=== Downloading invoice for order ID: " + orderId + " ===");

            // Use the service method that fetches all relationships
            Order order = orderService.getOrderByIdWithRelations(orderId);

            // Debug the loaded data
            System.out.println("Order loaded successfully: " + order.getOrderNumber());
            System.out.println("Items count: " + (order.getOrderItems() != null ? order.getOrderItems().size() : 0));
            System.out.println("Shipping Address: " + (order.getShippingAddress() != null ? "Present" : "Null"));
            System.out.println("Billing Address: " + (order.getBillingAddress() != null ? "Present" : "Null"));
            System.out.println("User: " + (order.getUser() != null ? "Present" : "Null"));

            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                order.getOrderItems().forEach(item -> {
                    System.out.println(" - " + item.getProductName() +
                            " | Qty: " + item.getQuantity() +
                            " | Price: $" + item.getPrice() +
                            " | Total: $" + item.getTotalPrice());
                });
            } else {
                System.out.println("WARNING: No order items found!");
            }

            byte[] invoicePdf = jasperReportService.generateInvoice(order);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "invoice-" + order.getOrderNumber() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            System.out.println("=== Invoice generated successfully ===");
            return new ResponseEntity<>(invoicePdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("ERROR generating invoice: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}