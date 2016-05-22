package io.chark.food.domain.comment;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment> {

    List<Comment> findByHidden(boolean hidden);
}
