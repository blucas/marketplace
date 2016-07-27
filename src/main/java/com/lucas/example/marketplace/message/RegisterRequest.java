package com.lucas.example.marketplace.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Encapsulates request to register an offer or a bid in the system.
 *
 * @author Brendt Lucas
 */
public class RegisterRequest {

    @NotNull
    @JsonProperty
    private Integer itemId;

    @NotNull
    @JsonProperty
    private Integer userId;

    @NotNull
    @Min(1)
    @JsonProperty
    private Integer quantity;

    @NotNull
    @Min(0)
    @JsonProperty
    private Integer pricePerUnit;

    public RegisterRequest() {
    }

    public RegisterRequest(Integer itemId, Integer userId, Integer quantity, Integer pricePerUnit) {
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
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
}
