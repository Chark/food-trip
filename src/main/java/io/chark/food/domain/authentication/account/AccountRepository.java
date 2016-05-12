package io.chark.food.domain.authentication.account;

import io.chark.food.domain.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}