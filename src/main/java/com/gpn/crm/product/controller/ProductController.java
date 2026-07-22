package com.gpn.crm.product.controller;

import com.gpn.crm.product.dto.ProductDto;
import com.gpn.crm.product.dto.ProductOffersDto;
import com.gpn.crm.product.dto.ProductPageDto;
import com.gpn.crm.product.dto.ProductQuery;
import com.gpn.crm.product.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductPageDto getProducts(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "15") @Min(1) @Max(50) int limit,
            @RequestParam(required = false) BigDecimal minAvailable,
            @RequestParam(required = false) String name
    ) {
        return productService.getProducts(new ProductQuery(page, limit, minAvailable, name));
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable long productId) {
        return productService.getProduct(productId);
    }

    @GetMapping("/{productId}/offers")
    public ProductOffersDto getProductOffers(@PathVariable long productId) {
        return productService.getProductOffers(productId);
    }
}
