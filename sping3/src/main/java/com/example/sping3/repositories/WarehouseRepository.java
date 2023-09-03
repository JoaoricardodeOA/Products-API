package com.example.sping3.repositories;

import com.example.sping3.models.ProductModel;
import com.example.sping3.models.WarehouseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseModel, Long> {
}
