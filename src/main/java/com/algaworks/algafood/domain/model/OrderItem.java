package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class OrderItem {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private BigDecimal valueUnitary;

    @Column(nullable = false)
    private BigDecimal valueTotal;

    private String observation;


    @ManyToOne
    @JoinColumn(nullable = false)
    private Ordered ordered;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}
