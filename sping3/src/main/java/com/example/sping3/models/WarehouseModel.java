package com.example.sping3.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_WAREHOUSES")
@Data
public class WarehouseModel extends RepresentationModel<ProductModel> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column
    @OneToMany
    private List<ProductModel> product;

    @Column(nullable = false,length = 100)
    private String WarehouseName;
}
