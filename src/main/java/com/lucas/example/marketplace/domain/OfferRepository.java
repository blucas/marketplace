package com.lucas.example.marketplace.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Brendt Lucas
 */
public interface OfferRepository extends CrudRepository<Offer, Long> {

    @Query(value = "select min(o.pricePerUnit) from Offer o where o.itemId = :itemId")
    Integer getCurrentOfferPrice(@Param("itemId") int itemId);

    List<Offer> findAllByUserId(int userId);

    @Query(value = "select * from Offer where item_id = :itemId and price_per_unit <= :pricePerUnit and quantity >= :quantity order by date_created asc limit 1", nativeQuery = true)
    Offer findFirstMatchingOffer(@Param("itemId") int itemId, @Param("pricePerUnit") int pricePerUnit, @Param("quantity") int quantity);
}
