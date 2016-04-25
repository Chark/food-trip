package io.chark.food.domain.authentication.account;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Account findByUsername(String username);
}