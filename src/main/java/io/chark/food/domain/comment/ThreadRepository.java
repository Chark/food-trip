package io.chark.food.domain.comment;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends BaseRepository<Thread> {
    Thread findByTitleLike(String title);
}
