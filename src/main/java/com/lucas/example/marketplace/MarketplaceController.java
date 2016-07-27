package com.lucas.example.marketplace;

import com.lucas.example.marketplace.domain.Bid;
import com.lucas.example.marketplace.domain.Offer;
import com.lucas.example.marketplace.domain.Order;
import com.lucas.example.marketplace.message.RegisterRequest;
import com.lucas.example.marketplace.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Brendt Lucas
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/marketplace")
@ComponentScan("com.lucas.example.marketplace")
public class MarketplaceController {

    @Autowired
    private MarketplaceService marketplaceService;

    /**
     * @param userId The buyer's user id
     * @return all the bids relating to the given buyer
     * @throws Exception
     */
    @RequestMapping(value = "/bids", method = RequestMethod.GET)
    List<Bid> getBids(@RequestParam("userId") int userId) throws Exception {
        return marketplaceService.getBids(userId);
    }

    /**
     * @param userId The seller's user id
     * @return all the offers relating to the seller
     * @throws Exception
     */
    @RequestMapping(value = "/offers", method = RequestMethod.GET)
    List<Offer> getOffers(@RequestParam("userId") int userId) throws Exception {
        return marketplaceService.getOffers(userId);
    }

    /**
     * @param itemId The item id
     * @return The highest price of all bids for the given item
     * @throws Exception
     */
    @RequestMapping(value = "/bids/{itemId}/currentPrice", method = RequestMethod.GET)
    Integer getCurrentBidPrice(@PathVariable int itemId) throws Exception {
        return marketplaceService.getCurrentBidPrice(itemId);
    }

    /**
     * @param itemId The item id
     * @return The lowest price of all offers for the given item
     * @throws Exception
     */
    @RequestMapping(value = "/offers/{itemId}/currentPrice", method = RequestMethod.GET)
    Integer getCurrentOfferPrice(@PathVariable int itemId) throws Exception {
        return marketplaceService.getCurrentOfferPrice(itemId);
    }

    /**
     * If the buyer is supplied, returns all the orders relating to the supplied buyer.<br>
     * If the buyer is omitted and the seller is supplied, the operation will return all orders relating to the supplied seller.<br>
     * If neither is true, an exception is thrown.
     *
     * @param buyerId The buyer's user id
     * @param sellerId The seller's user id
     * @return all orders relating to either the buyer or seller
     * @throws Exception
     */
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    List<Order> getOrders(@RequestParam(value = "buyerId", required = false) Integer buyerId,
                          @RequestParam(value = "sellerId", required = false) Integer sellerId) throws Exception {
        if (buyerId != null) {
            return marketplaceService.getOrdersByBuyer(buyerId);
        } else if (sellerId != null) {
            return marketplaceService.getOrdersBySeller(sellerId);
        } else {
            throw new IllegalArgumentException("One of 'buyerId' or 'sellerId' is required for this operation");
        }
    }

    /**
     * Register a bid in the marketplace.
     * Registering a bid may result in one or more {@link Order}s being generated and the bid being removed
     * from the marketplace.
     *
     * @param request The registration request
     * @throws Exception
     */
    @RequestMapping(value = "/bids", method = RequestMethod.POST)
    void registerBid(@RequestBody @Valid RegisterRequest request) throws Exception {
        marketplaceService.registerBid(request.getItemId(), request.getUserId(), request.getQuantity(),
                request.getPricePerUnit());
    }

    /**
     * Register an offer in the marketplace.
     * Registering an offer may result in one or more {@link Order}s being generated and the offer being removed
     * from the marketplace.
     *
     * @param request The registration request
     * @throws Exception
     */
    @RequestMapping(value = "/offers", method = RequestMethod.POST)
    void registerOffer(@RequestBody @Valid RegisterRequest request) throws Exception {
        marketplaceService.registerOffer(request.getItemId(), request.getUserId(), request.getQuantity(),
                request.getPricePerUnit());
    }

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceController.class, args);
    }
}
