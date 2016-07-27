package com.lucas.example.marketplace.service;

import com.lucas.example.marketplace.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Brendt Lucas
 */
@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerBid(int itemId, int userId, int quantity, int pricePerUnit) throws Exception {
        final Bid bid = bidRepository.save(new Bid(itemId, userId, quantity, pricePerUnit));

        final Offer offer =
                offerRepository.findFirstMatchingOffer(bid.getItemId(), bid.getPricePerUnit(), bid.getQuantity());

        if (offer != null) {
            generateOrders(offer);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerOffer(int itemId, int userId, int quantity, int pricePerUnit) throws Exception {
        final Offer offer = offerRepository.save(new Offer(itemId, userId, quantity, pricePerUnit));
        generateOrders(offer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Bid> getBids(int userId) throws Exception {
        return bidRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Offer> getOffers(int userId) throws Exception {
        return offerRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Order> getOrdersByBuyer(int buyerUserId) throws Exception {
        return orderRepository.findAllByBuyerId(buyerUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Order> getOrdersBySeller(int sellerUserId) throws Exception {
        return orderRepository.findAllBySellerId(sellerUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer getCurrentBidPrice(int itemId) throws Exception {
        return bidRepository.getCurrentBidPrice(itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer getCurrentOfferPrice(int itemId) throws Exception {
        return offerRepository.getCurrentOfferPrice(itemId);
    }

    private void generateOrder(Bid bid, Offer offer) {
        final Order order = orderRepository.save(new Order(bid.getItemId(), bid.getUserId(), offer.getUserId(),
                bid.getQuantity(), Math.min(bid.getPricePerUnit(), offer.getPricePerUnit())));

        // Reduce the the quantity
        offer.reduceQuantity(order.getQuantity());

        // Delete the bid
        bidRepository.delete(bid);
    }

    private void generateOrders(Offer offer) {
        // Attempt to generate orders for this offer
        Bid bid;
        do {
            // First the first matching bid
            bid = bidRepository.findFirstMatchingBid(offer.getItemId(), offer.getPricePerUnit(), offer.getQuantity());

            if (bid != null) {
                // Generate an order from the bid and offer
                generateOrder(bid, offer);
            }

            // continue if an order was generated and the offer's quantity is greater than zero
        } while (bid != null && offer.getQuantity() > 0);

        if (offer.getQuantity() == 0) {
            // The offer is no longer available
            offerRepository.delete(offer);
        }
    }
}
