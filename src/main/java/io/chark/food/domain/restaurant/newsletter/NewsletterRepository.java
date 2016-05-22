package io.chark.food.domain.restaurant.newsletter;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsletterRepository extends BaseRepository<Newsletter> {

    @Query("SELECT n FROM Newsletter n INNER JOIN n.offers o WHERE o.id = ?1")
    List<Newsletter> findByOfferId(long id);

    List<Newsletter> findByPublished(boolean published);
}