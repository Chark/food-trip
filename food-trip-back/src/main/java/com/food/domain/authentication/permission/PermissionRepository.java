package com.food.domain.authentication.permission;

import com.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {

    Permission findByAuthority(Permission.Authority authority);
}