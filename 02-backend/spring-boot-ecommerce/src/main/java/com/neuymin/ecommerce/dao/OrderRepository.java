package com.neuymin.ecommerce.dao;

import com.neuymin.ecommerce.entity.Order;
import com.neuymin.ecommerce.entity.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByDateCreatedDesc(@Param("email") String email, Pageable pageable);

}
