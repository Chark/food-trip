package io.chark.food.domain.restaurant;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    /**
     * Get restaurant by account id.
     *
     * @param id user account id.
     * @return restaurant or null if it was not found.
     */
    @Query("SELECT r FROM Restaurant r INNER JOIN r.accounts a WHERE a.id = ?1")
    Restaurant findByAccountId(long id);
}