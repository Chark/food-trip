package io.chark.food.domain.sandwich;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends BaseRepository<Basket> {

    @Query("SELECT b FROM Basket b INNER JOIN " +
            "b.sandwiches s WHERE " +
            "s.name LIKE CONCAT('%', ?1, '%')")
    List<Basket> findBySandwichName(String name);
}