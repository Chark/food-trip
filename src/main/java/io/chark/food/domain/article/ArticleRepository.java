package io.chark.food.domain.article;

import io.chark.food.domain.BaseRepository;
import io.chark.food.domain.authentication.account.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends BaseRepository<Account> {
}