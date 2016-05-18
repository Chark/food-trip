package io.chark.food.domain.thread.category;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadCategoryRepository extends BaseRepository<ThreadCategory> {
    ThreadCategory findByNameLike(String name);
}
