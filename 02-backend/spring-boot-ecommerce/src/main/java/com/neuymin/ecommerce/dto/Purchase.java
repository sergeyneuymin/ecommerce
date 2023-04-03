package com.neuymin.ecommerce.dto;

import com.neuymin.ecommerce.entity.Address;
import com.neuymin.ecommerce.entity.Customer;
import com.neuymin.ecommerce.entity.Order;
import com.neuymin.ecommerce.entity.OrderItem;
import lombok.Data;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;

    private Address shippingAddress;

    private Address billingAddress;

    private Order order;

    private Set<OrderItem> orderItems;

}
