package io.chark.food.domain.audit;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantAuditMessageRepository extends BaseRepository<RestaurantAuditMessage> {

    /**
     * Get a list of restaurant audit messages by restaurant id.
     *
     * @param id restaurant id.
     * @return list of audit messages.
     */
    List<RestaurantAuditMessage> findByRestaurantId(long id);
}