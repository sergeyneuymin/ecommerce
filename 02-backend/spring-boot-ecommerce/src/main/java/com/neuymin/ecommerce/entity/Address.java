package com.neuymin.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "street")
    private String street;

    @Column(name = "zipCode")
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn // join using primary keys
    private Order order;
}
