package com.example.sping3.controllers;

import com.example.sping3.dtos.ProductRecordDto;
import com.example.sping3.dtos.WarehouseRecordDto;
import com.example.sping3.models.ProductModel;
import com.example.sping3.models.WarehouseModel;
import com.example.sping3.repositories.ProductRepository;
import com.example.sping3.repositories.WarehouseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class WarehouseController {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    ProductRepository productRepository;
    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseModel> saveWarehouse(@RequestBody @Valid WarehouseRecordDto warehouseRecordDto){
        var warehouseModel = new WarehouseModel();
        BeanUtils.copyProperties(warehouseRecordDto, warehouseModel);
        List<ProductModel> productsModelList = new ArrayList<>();
        warehouseModel.setProduct(productsModelList);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseRepository.save(warehouseModel));
    }
    @PutMapping("/warehouses/{id}/products")
    public ResponseEntity<Object> saveProductWarehouse(@PathVariable(value = "id") Long id , @RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        Optional<WarehouseModel> warehouseModelOptional = warehouseRepository.findById(id);
        if(warehouseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazém não encontrado");
        }
        var warehouse = warehouseModelOptional.get();
        var list = warehouse.getProduct();
        productRepository.save(productModel);
        list.add(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseRepository.save(warehouse));
    }
}
