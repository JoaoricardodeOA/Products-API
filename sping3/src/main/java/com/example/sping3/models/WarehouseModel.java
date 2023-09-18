package com.example.sping3.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_WAREHOUSES")
public class WarehouseModel extends RepresentationModel<ProductModel> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Setter
    @Getter
    @Column
    @OneToMany
    private List<ProductModel> product;
    @Setter
    @Getter
    @Column(nullable = false,length = 100)
    private String WarehouseName;
}
