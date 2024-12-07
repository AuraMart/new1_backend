//package com.dailycodework.dreamshops.model;
//
//import java.math.BigDecimal;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//public class OrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private int quantity;
//    private BigDecimal price;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    @JsonBackReference
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    public OrderItem(Order order, Product product, int quantity) {
//        this.order = order;
//        this.product = product;
//        this.quantity = quantity;
//        //this.price = price;
//
//    }
//}




package com.dailycodework.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
}
