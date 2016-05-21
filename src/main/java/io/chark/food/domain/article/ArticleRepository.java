package io.chark.food.domain.article;

import io.chark.food.domain.BaseRepository;
import io.chark.food.domain.authentication.account.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends BaseRepository<Article> {

    /**
     * Find article by id and restaurant id.
     *
     * @param id           article id.
     * @param restaurantId restaurant to which the article belongs.
     * @return article or null.
     */
    Article findByIdAndRestaurantId(long id, long restaurantId);
}