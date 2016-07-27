package com.lucas.example.marketplace.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Brendt Lucas
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAllByBuyerId(int buyerId);

    List<Order> findAllBySellerId(int sellerId);
}
