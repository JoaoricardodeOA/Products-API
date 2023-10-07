package com.example.sping3.controllers;

import com.example.sping3.dtos.ProductRecordDto;
import com.example.sping3.models.ProductModel;
import com.example.sping3.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto,
                                                    Errors errors){
        if(productRecordDto.name()==null||productRecordDto.name().isEmpty()){
            return new ResponseEntity("Não é possível um produto sem nome", HttpStatus.BAD_REQUEST);
        }
        if(productRecordDto.value()==null||productRecordDto.value().equals(0)){
            return new ResponseEntity("Não é possível um produto sem valor", HttpStatus.BAD_REQUEST);
        }
        if(productRecordDto.value().compareTo(BigDecimal.valueOf(0))<0){
            return new ResponseEntity("Valor de produto negativo", HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity("erro no servidor", HttpStatus.BAD_REQUEST);
        }

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getProducts(){
        List<ProductModel> productModelList = productRepository.findAll();
        if(!productModelList.isEmpty()){
            for (ProductModel productModel:
                 productModelList) {
                Long id = productModel.getId();
                productModel.add(linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productModelList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "id") Long id){
        Optional<ProductModel> product = productRepository.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        product.get().add(linkTo(methodOn(ProductController.class).getProducts()).withRel("Lista de Produtos"));
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> setProductById(@PathVariable(value = "id") Long id,
                                                 @RequestBody @Valid ProductRecordDto productRecordDto,
                                                 Errors errors){
        if(productRecordDto.name()==null||productRecordDto.name().isEmpty()){
            return new ResponseEntity("Não é possível um produto sem nome", HttpStatus.BAD_REQUEST);
        }
        if(productRecordDto.value()==null||productRecordDto.value().equals(0)){
            return new ResponseEntity("Não é possível um produto sem valor", HttpStatus.BAD_REQUEST);
        }
        if(productRecordDto.value().compareTo(BigDecimal.valueOf(0))<0){
            return new ResponseEntity("Valor de produto negativo", HttpStatus.BAD_REQUEST);
        }
        if (errors.hasErrors()) {
            return new ResponseEntity("erro no servidor", HttpStatus.BAD_REQUEST);
        }
        Optional<ProductModel> product = productRepository.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto,productModel);
        productModel.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable(value = "id") Long id){
        Optional<ProductModel> product = productRepository.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productRepository.delete(product.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado");
    }

}
