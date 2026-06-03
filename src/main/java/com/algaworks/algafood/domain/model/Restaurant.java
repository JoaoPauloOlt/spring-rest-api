package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.ValueZeroIncludeDescription;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ValueZeroIncludeDescription(valueField = "shippingFee",
descriptionField = "name", descriptionRequired = "free shipping")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurant {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "shipping_fee", nullable = false)
    private BigDecimal shippingFee;

    @ManyToOne
    @JoinColumn(name = "kitchen_id", nullable = false)
    private Kitchen kitchen;

    @Embedded
    private Address address;

    private Boolean active = Boolean.TRUE;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dateRegister;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dateUpdate;

    @ManyToMany
    @JoinTable(name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Product> products = new ArrayList<>();

    private Boolean open = Boolean.FALSE;

    public void open(){
        setOpen(true);
    }

    public void close(){
        setOpen(false);
    }

    public void activate(){
        setActive(true);
    }

    public void disable(){
        setActive(false);
    }

    public boolean deletePaymentMethod(PaymentMethod paymentMethod){
        return getPaymentMethods().remove(paymentMethod);
    }

    public boolean createPaymentMethod(PaymentMethod paymentMethod){
        return getPaymentMethods().add(paymentMethod);
    }
}
