package com.lucas.example.marketplace.service;

import com.lucas.example.marketplace.domain.Bid;
import com.lucas.example.marketplace.domain.Offer;
import com.lucas.example.marketplace.domain.Order;

import java.util.List;

/**
 * @author Brendt Lucas
 */
public interface MarketplaceService {

    /**
     * Register a bid in the marketplace.
     * Registering a bid may result in one or more {@link Order}s being generated and the bid being removed
     * from the marketplace.
     *
     * @param itemId The item id
     * @param userId The buyer's user id
     * @param quantity The quantity to buy
     * @param pricePerUnit The price
     * @throws Exception
     */
    void registerBid(int itemId, int userId, int quantity, int pricePerUnit) throws Exception;

    /**
     * Register an offer in the marketplace.
     * Registering an offer may result in one or more {@link Order}s being generated and the offer being removed
     * from the marketplace.
     *
     * @param itemId The item id
     * @param userId The seller's user id
     * @param quantity The quantity to sell
     * @param pricePerUnit The price
     * @throws Exception
     */
    void registerOffer(int itemId, int userId, int quantity, int pricePerUnit) throws Exception;

    /**
     * @param userId The buyer's user id
     * @return all the bids registered by the given user
     * @throws Exception
     */
    List<Bid> getBids(int userId) throws Exception;

    /**
     * @param userId The seller's user id
     * @return all the offers registered by the given user
     * @throws Exception
     */
    List<Offer> getOffers(int userId) throws Exception;

    /**
     * @param buyerUserId The buyer's user id
     * @return all the orders relating to the given buyer
     * @throws Exception
     */
    List<Order> getOrdersByBuyer(int buyerUserId) throws Exception;

    /**
     * @param sellerUserId The seller's user id
     * @return all the orders relating to the given seller
     * @throws Exception
     */
    List<Order> getOrdersBySeller(int sellerUserId) throws Exception;

    /**
     * @param itemId The item id
     * @return The highest price of all bids for the given item
     * @throws Exception
     */
    Integer getCurrentBidPrice(int itemId) throws Exception;

    /**
     * @param itemId The item id
     * @return The lowest price of all bids for the given item
     * @throws Exception
     */
    Integer getCurrentOfferPrice(int itemId) throws Exception;
}
