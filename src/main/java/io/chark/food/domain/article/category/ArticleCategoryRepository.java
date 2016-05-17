package io.chark.food.domain.article.category;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCategoryRepository extends BaseRepository<ArticleCategory> {

    ArticleCategory findByTitleLike(String title);
}