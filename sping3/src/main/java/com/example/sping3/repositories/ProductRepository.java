package com.example.sping3.repositories;

import com.example.sping3.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

}
