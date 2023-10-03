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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class WarehouseController {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    ProductRepository productRepository;
    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseModel> saveWarehouse(@RequestBody @Valid WarehouseRecordDto warehouseRecordDto, Errors errors){

        if(warehouseRecordDto.WarehouseName() == null || warehouseRecordDto.WarehouseName().isEmpty()){
            return new ResponseEntity("Nome vazio",HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
        var warehouseModel = new WarehouseModel();
        BeanUtils.copyProperties(warehouseRecordDto, warehouseModel);
        List<ProductModel> productsModelList = new ArrayList<>();
        warehouseModel.setProduct(productsModelList);
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseRepository.save(warehouseModel));
    }
    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseModel>> getWarehouses(){
        List<WarehouseModel> warehouseModelList= warehouseRepository.findAll();
        if(!warehouseModelList.isEmpty()){
            for (WarehouseModel warehouseModel:
                    warehouseModelList) {
                Long id = warehouseModel.getId();
                warehouseModel.add(linkTo(methodOn(WarehouseController.class).getWarehouseById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(warehouseModelList);
    }
    @GetMapping("/warehouses/{id}")
    public ResponseEntity<Object> getWarehouseById(@PathVariable(value = "id") Long id){

        Optional<WarehouseModel> warehouseModel = warehouseRepository.findById(id);
        if(warehouseModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazém não encontrado");
        }
        warehouseModel.get().add(linkTo(methodOn(WarehouseController.class).getWarehouses()).withRel("Lista de Armazéns"));
        return ResponseEntity.status(HttpStatus.OK).body(warehouseModel.get());
    }
    @PutMapping("/warehouses/{id}")
    public ResponseEntity<Object> updateWarehouse(@PathVariable(value = "id") Long id,
                                                  @RequestBody @Valid WarehouseRecordDto warehouseRecordDto,
                                                  Errors errors){
        if(warehouseRecordDto.WarehouseName() == null||warehouseRecordDto.WarehouseName().isEmpty()){
            return new ResponseEntity("Nome vazio",HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity("erro no servidor", HttpStatus.BAD_REQUEST);
        }
        Optional<WarehouseModel> warehouse = warehouseRepository.findById(id);
        if(warehouse.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazém não encontrado");
        }
        var warehouseModel = warehouse.get();
        BeanUtils.copyProperties(warehouseRecordDto,warehouseModel);
        warehouseModel.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(warehouseRepository.save(warehouseModel));
    }
    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity<Object> deleteWarehouseById(@PathVariable(value = "id") Long id, Errors errors){
        if (errors.hasErrors()) {
            return new ResponseEntity("erro no servidor", HttpStatus.BAD_REQUEST);
        }
        Optional<WarehouseModel> warehouseModelOptional = warehouseRepository.findById(id);
        if(warehouseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Armazém não encontrado");
        }
        warehouseRepository.delete(warehouseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Armazém deletado");
    }
    @PutMapping("/warehouses/{id}/products")
    public ResponseEntity<Object> saveProductWarehouse(@PathVariable(value = "id") Long id ,
                                                       @RequestBody @Valid ProductRecordDto productRecordDto,
                                                       Errors errors){
        if(productRecordDto.name() == null||productRecordDto.name().isEmpty()){
            return new ResponseEntity("Não é possível um produto sem nome", HttpStatus.BAD_REQUEST);
        }
        if(productRecordDto.value() == null||productRecordDto.value().equals(0)){
            return new ResponseEntity("Não é possível um produto sem valor", HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity("erro no servidor", HttpStatus.BAD_REQUEST);
        }
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
