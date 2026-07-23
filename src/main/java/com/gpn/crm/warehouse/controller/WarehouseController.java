package com.gpn.crm.warehouse.controller;

import com.gpn.crm.warehouse.dto.WarehouseDto;
import com.gpn.crm.warehouse.service.WarehouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public List<WarehouseDto> getWarehouses() {
        return warehouseService.getWarehouses();
    }
}
