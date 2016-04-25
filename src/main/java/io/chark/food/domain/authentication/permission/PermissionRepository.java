package io.chark.food.domain.authentication.permission;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {

    Permission findByAuthority(Permission.Authority authority);
}