package io.chark.food.domain.thread;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.lang.*;

@Repository
public interface ThreadRepository extends BaseRepository<Thread> {
    Thread findByTitleLike(String title);
}
