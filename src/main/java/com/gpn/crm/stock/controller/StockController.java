package com.gpn.crm.stock.controller;

import com.gpn.crm.stock.dto.StockAllQuery;
import com.gpn.crm.stock.dto.StockOfferDto;
import com.gpn.crm.stock.dto.StockPageDto;
import com.gpn.crm.stock.dto.StockQuery;
import com.gpn.crm.stock.service.StockService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public StockPageDto getStocks(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "15") @Min(1) @Max(50) int limit,
            @RequestParam(defaultValue = "true") boolean details,
            @RequestParam(required = false) BigDecimal minAvailable,
            @RequestParam(required = false) String sku
    ) {
        return stockService.getStocks(new StockQuery(page, limit, details, minAvailable, sku));
    }

    @GetMapping("/stocks/all")
    public List<StockOfferDto> getAllStocks(
            @RequestParam(defaultValue = "true") boolean details,
            @RequestParam(required = false) BigDecimal minAvailable,
            @RequestParam(required = false) String sku
    ) {
        return stockService.getAllStocks(new StockAllQuery(details, minAvailable, sku));
    }
}
