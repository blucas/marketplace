package com.lucas.example.marketplace.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Brendt Lucas
 */
@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic(optional = false)
    private int itemId;

    @Basic(optional = false)
    private int userId;

    @Basic(optional = false)
    private int quantity;

    @Basic(optional = false)
    private int pricePerUnit;

    @Temporal(TemporalType.TIMESTAMP)
    @Basic(optional = false)
    private Date dateCreated;

    Offer() {
    }

    public Offer(int itemId, int userId, int quantity, int pricePerUnit) {
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        dateCreated = new Date();
    }

    public long getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void reduceQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
