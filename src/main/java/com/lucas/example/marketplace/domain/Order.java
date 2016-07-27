package com.lucas.example.marketplace.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Brendt Lucas
 */
@Entity(name = "OrderItem")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int itemId;

    private int buyerId;

    private int sellerId;

    private int quantity;

    private int pricePerUnit;

    Order() {
    }

    public Order(int itemId, int buyerId, int sellerId, int quantity, int pricePerUnit) {
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public long getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }
}
