package io.chark.food.domain.restaurant;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InvitationRepository extends BaseRepository<Invitation> {

    /**
     * Delete invitation by id and restaurant id.
     *
     * @param restaurantId restaurant id.
     * @param id           invitation id.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Invitation i WHERE i.restaurant.id = ?1 AND i.id = ?2")
    void deleteByRestaurantAndId(long restaurantId, long id);

    /**
     * Delete all invitations assigned to this specific account.
     *
     * @param id account id.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Invitation i WHERE i.account.id = ?1")
    void deleteByAccountId(long id);

    /**
     * Get invitation by id and account id.
     *
     * @param accountId account id.
     * @param id        invitation id.
     */
    Invitation findByAccountIdAndId(long accountId, long id);
}