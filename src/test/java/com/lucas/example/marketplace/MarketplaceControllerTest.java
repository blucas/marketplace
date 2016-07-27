package com.lucas.example.marketplace;

import com.lucas.example.marketplace.domain.Bid;
import com.lucas.example.marketplace.domain.Offer;
import com.lucas.example.marketplace.domain.Order;
import com.lucas.example.marketplace.message.RegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Brendt Lucas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MarketplaceController.class)
@WebIntegrationTest
public class MarketplaceControllerTest {

    private RestTemplate restTemplate = new TestRestTemplate();

    // Make sure the service is not currently running when executing these tests
    private String baseUrl = "http://localhost:8080/marketplace";

    @Test
    public void testRegisterBid() throws Exception {

        final RegisterRequest request = new RegisterRequest(-1, 1, 5, 10);
        final ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/bids", request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterOffer() throws Exception {

        final RegisterRequest request = new RegisterRequest(-2, 1, 5, 10);
        final ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/offers", request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCurrentBidPrice() throws Exception {

        RegisterRequest request = new RegisterRequest(-3, 1, 5, 10);
        restTemplate.postForEntity(baseUrl + "/bids", request, Void.class);

        ResponseEntity<Integer> response = restTemplate.getForEntity(baseUrl + "/bids/{itemId}/currentPrice",
                Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(request.getPricePerUnit(), currentPrice.intValue());

        final int expectedPrice = 999;
        request = new RegisterRequest(-3, 1, 5, expectedPrice);
        restTemplate.postForEntity(baseUrl + "/bids", request, Void.class);

        response = restTemplate.getForEntity(baseUrl + "/bids/{itemId}/currentPrice", Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(request.getPricePerUnit(), currentPrice.intValue());

        request = new RegisterRequest(-3, 1, 5, 50);
        restTemplate.postForEntity(baseUrl + "/bids", request, Void.class);

        response = restTemplate.getForEntity(baseUrl + "/bids/{itemId}/currentPrice", Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(expectedPrice, currentPrice.intValue());
    }

    @Test
    public void testCurrentOfferPrice() throws Exception {

        RegisterRequest request = new RegisterRequest(-4, 1, 5, 10);
        restTemplate.postForEntity(baseUrl + "/offers", request, Void.class);

        ResponseEntity<Integer> response = restTemplate.getForEntity(baseUrl + "/offers/{itemId}/currentPrice",
                Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(request.getPricePerUnit(), currentPrice.intValue());

        final int expectedPrice = request.getPricePerUnit();
        request = new RegisterRequest(-4, 1, 5, 999);
        restTemplate.postForEntity(baseUrl + "/offers", request, Void.class);

        response = restTemplate.getForEntity(baseUrl + "/offers/{itemId}/currentPrice", Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(expectedPrice, currentPrice.intValue());

        request = new RegisterRequest(-4, 1, 5, 5);
        restTemplate.postForEntity(baseUrl + "/offers", request, Void.class);

        response = restTemplate.getForEntity(baseUrl + "/offers/{itemId}/currentPrice", Integer.class, request.getItemId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        currentPrice = response.getBody();
        assertNotNull(currentPrice);
        assertEquals(request.getPricePerUnit(), currentPrice.intValue());
    }

    @Test
    public void testGetBids() throws Exception {

        final RegisterRequest request = new RegisterRequest(-5, -1, 5, 10);
        restTemplate.postForEntity(baseUrl + "/bids", request, Void.class);

        final ParameterizedTypeReference<List<Bid>> responseType = new ParameterizedTypeReference<List<Bid>>() {
        };
        final ResponseEntity<List<Bid>> response = restTemplate.exchange(baseUrl + "/bids?userId={userId}",
                HttpMethod.GET, null, responseType, request.getUserId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final List<Bid> bids = response.getBody();
        assertNotNull(bids);
        assertEquals(1, bids.size());
        assertEquals(request.getPricePerUnit(), bids.get(0).getPricePerUnit());
        assertEquals(request.getQuantity(), bids.get(0).getQuantity());
        assertEquals(request.getItemId(), bids.get(0).getItemId());
    }

    @Test
    public void testGetOffers() throws Exception {

        final RegisterRequest request = new RegisterRequest(-6, -2, 5, 10);
        restTemplate.postForEntity(baseUrl + "/offers", request, Void.class);

        final ParameterizedTypeReference<List<Offer>> responseType = new ParameterizedTypeReference<List<Offer>>() {
        };
        final ResponseEntity<List<Offer>> response = restTemplate.exchange(baseUrl + "/offers?userId={userId}",
                HttpMethod.GET, null, responseType, request.getUserId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final List<Offer> offers = response.getBody();
        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals(request.getPricePerUnit(), offers.get(0).getPricePerUnit());
        assertEquals(request.getQuantity(), offers.get(0).getQuantity());
        assertEquals(request.getItemId(), offers.get(0).getItemId());
    }

    @Test
    public void testGetOrders() throws Exception {

        RegisterRequest bidRequest = new RegisterRequest(1, 3, 9, 5);
        final ResponseEntity<Void> bidResponse = restTemplate.postForEntity(baseUrl + "/bids", bidRequest, Void.class);
        assertEquals(HttpStatus.OK, bidResponse.getStatusCode());

        RegisterRequest offerRequest = new RegisterRequest(1, 2, 10, 5);
        final ResponseEntity<Void> offerResponse = restTemplate.postForEntity(baseUrl + "/offers", offerRequest, Void.class);
        assertEquals(HttpStatus.OK, offerResponse.getStatusCode());

        // Assert that the order was generated and matches the respective bid
        final ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<List<Order>>() {
        };
        ResponseEntity<List<Order>> response = restTemplate.exchange(baseUrl + "/orders?buyerId={buyerId}",
                HttpMethod.GET, null, responseType, bidRequest.getUserId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Order> ordersByBuyer = response.getBody();
        assertNotNull(ordersByBuyer);
        assertEquals(1, ordersByBuyer.size());
        assertEquals(bidRequest.getItemId(), ordersByBuyer.get(0).getItemId());
        assertEquals(bidRequest.getUserId(), ordersByBuyer.get(0).getBuyerId());
        assertEquals(offerRequest.getUserId(), ordersByBuyer.get(0).getSellerId());
        assertEquals(bidRequest.getQuantity(), ordersByBuyer.get(0).getQuantity());
        assertEquals(Math.min(bidRequest.getPricePerUnit(), offerRequest.getPricePerUnit()),
                ordersByBuyer.get(0).getPricePerUnit());

        // Assert that the order was generated and matches the respective offer
        response = restTemplate.exchange(baseUrl + "/orders?sellerId={sellerId}",
                HttpMethod.GET, null, responseType, offerRequest.getUserId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Order> ordersBySeller = response.getBody();
        assertNotNull(ordersBySeller);
        assertEquals(1, ordersBySeller.size());
        assertEquals(offerRequest.getItemId(), ordersBySeller.get(0).getItemId());
        assertEquals(bidRequest.getUserId(), ordersBySeller.get(0).getBuyerId());
        assertEquals(offerRequest.getUserId(), ordersBySeller.get(0).getSellerId());
        assertEquals(bidRequest.getQuantity(), ordersBySeller.get(0).getQuantity());
        assertEquals(Math.min(bidRequest.getPricePerUnit(), offerRequest.getPricePerUnit()),
                ordersBySeller.get(0).getPricePerUnit());

        // Assert that the bid has been removed
        final ResponseEntity<List<Bid>> getBidsResponse = restTemplate.exchange(baseUrl + "/bids?userId={userId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Bid>>() {
                }, bidRequest.getUserId());

        assertEquals(HttpStatus.OK, getBidsResponse.getStatusCode());

        final List<Bid> bids = getBidsResponse.getBody();
        assertNotNull(bids);
        assertTrue(bids.isEmpty());

        // Assert that the offer has still exists but that the quantity has been reduced by the bid quantity
        final ResponseEntity<List<Offer>> getOffersResponse = restTemplate.exchange(baseUrl + "/offers?userId={userId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Offer>>() {
                }, offerRequest.getUserId());

        assertEquals(HttpStatus.OK, getOffersResponse.getStatusCode());

        final List<Offer> offers = getOffersResponse.getBody();
        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals(offerRequest.getItemId(), offers.get(0).getItemId());
        assertEquals(offerRequest.getPricePerUnit(), offers.get(0).getPricePerUnit());
        assertEquals(offerRequest.getUserId(), offers.get(0).getUserId());
        assertEquals(offerRequest.getQuantity() - bidRequest.getQuantity(), offers.get(0).getQuantity());
    }
}
