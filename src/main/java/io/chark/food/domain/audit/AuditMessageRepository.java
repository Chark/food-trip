package io.chark.food.domain.audit;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditMessageRepository extends BaseRepository<AuditMessage> {

    /**
     * Get a list of audit messages by account id.
     *
     * @param id account id.
     * @return list of audit messages.
     */
    List<AuditMessage> findByAccountId(long id);
}