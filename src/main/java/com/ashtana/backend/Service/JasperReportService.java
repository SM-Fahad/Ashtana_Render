package com.ashtana.backend.Service;

import com.ashtana.backend.Entity.Order;
import com.ashtana.backend.Entity.OrderItem;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
//@Service
//public class JasperReportService {
//
//    public byte[] generateInvoice(Order order) {
//        try {
//            System.out.println("=== Starting invoice generation for order: " + order.getOrderNumber() + " ===");
//
//            // Load the JRXML template from resources
//            InputStream templateStream = new ClassPathResource("reports/invoice.jrxml").getInputStream();
//            System.out.println("JRXML template loaded successfully");
//
//            // Compile the report
//            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
//            System.out.println("JRXML compiled successfully");
//
//            // Create parameters
//            Map<String, Object> parameters = createParameters(order);
//
//            // Create data source
//            JRBeanCollectionDataSource itemsDataSource = createDataSource(order);
//            parameters.put("itemsDataSource", itemsDataSource);
//
//            System.out.println("Parameters and data source created");
//
//            // Generate report
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
//            System.out.println("Report filled successfully");
//
//            // Export to PDF
//            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
//            System.out.println("PDF exported successfully, size: " + pdfBytes.length + " bytes");
//
//            return pdfBytes;
//
//        } catch (Exception e) {
//            System.err.println("Error generating invoice: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Failed to generate invoice: " + e.getMessage(), e);
//        }
//    }
//
//    private Map<String, Object> createParameters(Order order) {
//        Map<String, Object> parameters = new HashMap<>();
//
//        // Debug order data
//        System.out.println("Order details:");
//        System.out.println(" - Number: " + order.getOrderNumber());
//        System.out.println(" - Date: " + order.getOrderDate());
//        System.out.println(" - Customer: " + (order.getShippingAddress() != null ? order.getShippingAddress().getRecipientName() : "null"));
//        System.out.println(" - Items count: " + (order.getOrderItems() != null ? order.getOrderItems().size() : 0));
//
//        parameters.put("ORDER_NUMBER", order.getOrderNumber());
//        parameters.put("ORDER_DATE", order.getOrderDate() != null ? order.getOrderDate().toString() : "Unknown date");
//        parameters.put("CUSTOMER_NAME", order.getShippingAddress() != null ? order.getShippingAddress().getRecipientName() : "Unknown Customer");
//        parameters.put("CUSTOMER_EMAIL", order.getUser() != null ? order.getUser().getEmail() : "No email");
//        parameters.put("CUSTOMER_PHONE", order.getShippingAddress() != null ? order.getShippingAddress().getPhone() : "No phone");
//        parameters.put("SHIPPING_ADDRESS", formatAddress(order.getShippingAddress()));
//        parameters.put("BILLING_ADDRESS", formatAddress(order.getBillingAddress()));
//        parameters.put("PAYMENT_METHOD", order.getPaymentMethod() != null ? order.getPaymentMethod() : "Unknown");
//        parameters.put("PAYMENT_STATUS", order.getPaymentStatus() != null ? order.getPaymentStatus() : "Unknown");
//        parameters.put("SUBTOTAL", order.getSubtotalAmount() != null ? order.getSubtotalAmount() : 0.0);
//        parameters.put("SHIPPING_COST", order.getShippingCost() != null ? order.getShippingCost() : 0.0);
//        parameters.put("TAX_AMOUNT", order.getTaxAmount() != null ? order.getTaxAmount() : 0.0);
//        parameters.put("DISCOUNT_AMOUNT", order.getDiscountAmount() != null ? order.getDiscountAmount() : 0.0);
//        parameters.put("TOTAL_AMOUNT", order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
//
//        return parameters;
//    }
//
//    private JRBeanCollectionDataSource createDataSource(Order order) {
//        List<Map<String, Object>> reportData = new ArrayList<>();
//
//        if (order.getOrderItems() != null) {
//            System.out.println("Processing " + order.getOrderItems().size() + " order items:");
//
//            for (OrderItem item : order.getOrderItems()) {
//                Map<String, Object> itemData = new HashMap<>();
//
//                String productName = item.getProductName() != null ? item.getProductName() : "Unknown Product";
//                Integer quantity = item.getQuantity() != null ? item.getQuantity() : 0;
//                Double price = item.getPrice() != null ? item.getPrice() : 0.0;
//                Double total = item.getTotalPrice() != null ? item.getTotalPrice() : 0.0;
//
//                System.out.println(" - Item: " + productName + ", Qty: " + quantity + ", Price: " + price + ", Total: " + total);
//
//                itemData.put("productName", productName);
//                itemData.put("quantity", quantity);
//                itemData.put("price", price);
//                itemData.put("total", total);
//                reportData.add(itemData);
//            }
//        } else {
//            System.out.println("No order items found!");
//        }
//
//        return new JRBeanCollectionDataSource(reportData);
//    }
//
//    private String formatAddress(com.ashtana.backend.Entity.Address address) {
//        if (address == null) {
//            return "Address not available";
//        }
//
//        return String.format("%s, %s, %s %s, %s",
//                address.getStreet() != null ? address.getStreet() : "",
//                address.getCity() != null ? address.getCity() : "",
//                address.getState() != null ? address.getState() : "",
//                address.getPostalCode() != null ? address.getPostalCode() : "",
//                address.getCountry() != null ? address.getCountry() : ""
//        );
//    }
//}

@Service
public class JasperReportService {

    public byte[] generateInvoice(Order order) {
        try {
            System.out.println("=== Starting invoice generation for order: " + order.getOrderNumber() + " ===");

            // Load the JRXML template from resources
            InputStream templateStream = new ClassPathResource("reports/invoice.jrxml").getInputStream();
            System.out.println("JRXML template loaded successfully");

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
            System.out.println("JRXML compiled successfully");

            // Create parameters
            Map<String, Object> parameters = createParameters(order);

            // Create data source - THIS IS THE MAIN DATA SOURCE NOW
            JRBeanCollectionDataSource dataSource = createDataSource(order);

            System.out.println("Parameters and data source created");

            // Generate report - PASS DATA SOURCE AS THE THIRD PARAMETER
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("Report filled successfully");

            // Export to PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            System.out.println("PDF exported successfully, size: " + pdfBytes.length + " bytes");

            return pdfBytes;

        } catch (Exception e) {
            System.err.println("Error generating invoice: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate invoice: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createParameters(Order order) {
        Map<String, Object> parameters = new HashMap<>();

        // Debug order data
        System.out.println("Order details:");
        System.out.println(" - Number: " + order.getOrderNumber());
        System.out.println(" - Date: " + order.getOrderDate());
        System.out.println(" - Customer: " + (order.getShippingAddress() != null ? order.getShippingAddress().getRecipientName() : "null"));
        System.out.println(" - Items count: " + (order.getOrderItems() != null ? order.getOrderItems().size() : 0));

        parameters.put("ORDER_NUMBER", order.getOrderNumber());
        parameters.put("ORDER_DATE", order.getOrderDate() != null ? order.getOrderDate().toString() : "Unknown date");
        parameters.put("CUSTOMER_NAME", order.getShippingAddress() != null ? order.getShippingAddress().getRecipientName() : "Unknown Customer");
        parameters.put("CUSTOMER_EMAIL", order.getUser() != null ? order.getUser().getEmail() : "No email");
        parameters.put("CUSTOMER_PHONE", order.getShippingAddress() != null ? order.getShippingAddress().getPhone() : "No phone");
        parameters.put("SHIPPING_ADDRESS", formatAddress(order.getShippingAddress()));
        parameters.put("BILLING_ADDRESS", formatAddress(order.getBillingAddress()));
        parameters.put("PAYMENT_METHOD", order.getPaymentMethod() != null ? order.getPaymentMethod() : "Unknown");
        parameters.put("PAYMENT_STATUS", order.getPaymentStatus() != null ? order.getPaymentStatus() : "Unknown");
        parameters.put("SUBTOTAL", order.getSubtotalAmount() != null ? order.getSubtotalAmount() : 0.0);
        parameters.put("SHIPPING_COST", order.getShippingCost() != null ? order.getShippingCost() : 0.0);
        parameters.put("TAX_AMOUNT", order.getTaxAmount() != null ? order.getTaxAmount() : 0.0);
        parameters.put("DISCOUNT_AMOUNT", order.getDiscountAmount() != null ? order.getDiscountAmount() : 0.0);
        parameters.put("TOTAL_AMOUNT", order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);

        return parameters;
    }

    private JRBeanCollectionDataSource createDataSource(Order order) {
        List<Map<String, Object>> reportData = new ArrayList<>();

        if (order.getOrderItems() != null) {
            System.out.println("Processing " + order.getOrderItems().size() + " order items:");

            for (OrderItem item : order.getOrderItems()) {
                Map<String, Object> itemData = new HashMap<>();

                String productName = item.getProductName() != null ? item.getProductName() : "Unknown Product";
                Integer quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                Double price = item.getPrice() != null ? item.getPrice() : 0.0;
                Double total = item.getTotalPrice() != null ? item.getTotalPrice() : 0.0;

                System.out.println(" - Item: " + productName + ", Qty: " + quantity + ", Price: " + price + ", Total: " + total);

                itemData.put("productName", productName);
                itemData.put("quantity", quantity);
                itemData.put("price", price);
                itemData.put("total", total);
                reportData.add(itemData);
            }
        } else {
            System.out.println("No order items found!");
        }

        return new JRBeanCollectionDataSource(reportData);
    }

    private String formatAddress(com.ashtana.backend.Entity.Address address) {
        if (address == null) {
            return "Address not available";
        }

        return String.format("%s, %s, %s %s, %s",
                address.getStreet() != null ? address.getStreet() : "",
                address.getCity() != null ? address.getCity() : "",
                address.getState() != null ? address.getState() : "",
                address.getPostalCode() != null ? address.getPostalCode() : "",
                address.getCountry() != null ? address.getCountry() : ""
        );
    }
}