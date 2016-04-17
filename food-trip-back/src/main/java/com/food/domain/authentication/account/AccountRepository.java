package com.food.domain.authentication.account;

import com.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Account findByUsername(String username);
}