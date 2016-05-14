package io.chark.food.domain.authentication.permission;

import io.chark.food.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {

    /**
     * Find a specific permission by authority.
     *
     * @param authority permission authority.
     * @return a permission or null if it was not found.
     */
    Permission findByAuthority(Permission.Authority authority);

    /**
     * Get a set of permissions by querying them by provided authority list.
     *
     * @param authorities authorities to query the permissions by.
     * @return set of permissions.
     */
    Set<Permission> findByAuthorityIn(Permission.Authority... authorities);
}