package com.example.sping3.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "TB_PRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(length = 50,nullable = false,columnDefinition = "varchar(50)")
    private String name;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(columnDefinition = "varchar(255)")
    private String description;



}
