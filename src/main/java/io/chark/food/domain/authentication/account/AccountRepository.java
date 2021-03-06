package io.chark.food.domain.authentication.account;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    /**
     * Query a list of accounts by their username.
     *
     * @param username username which is to be used in query.
     * @return list of accounts.
     */
    @Query("SELECT a FROM Account a WHERE a.username = LOWER(?1)")
    Account findByUsername(String username);

    /**
     * Get a list of accounts whose ids are not in the provided list.
     *
     * @param ids list of ids to exclude.
     * @return account list
     */
    List<Account> findByIdNotIn(long... ids);

    /**
     * Count registered number of accounts for date interval.
     *
     * @param start date interval start.
     * @param end   date interval end.
     * @return count of accounts per day.
     */
    @Query("SELECT COUNT(a) FROM Account a " +
            "WHERE a.registrationDate BETWEEN ?1 AND ?2")
    Long countAccountsByDate(Date start, Date end);
}