package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.domain.enums.StatusOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Ordered {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false)
    private BigDecimal shippingFee;

    @Column(nullable = false)
    private BigDecimal valueTotal;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dateCreation;

    @CreationTimestamp
    @Column(columnDefinition = "datetime")
    private LocalDateTime dateConfirmation;

    @CreationTimestamp
    @Column(columnDefinition = "datetime")
    private LocalDateTime dateCancellation;

    @CreationTimestamp
    @Column(columnDefinition = "datetime")
    private LocalDateTime dateDelivery;

    @Embedded
    private Address addressDelivery;

    private StatusOrder status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_client_id", nullable = false)
    private User client;

    @OneToMany(mappedBy = "ordered")
    private List<OrderItem> items = new ArrayList<>();
}
