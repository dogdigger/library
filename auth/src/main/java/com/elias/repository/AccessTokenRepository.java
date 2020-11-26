package com.elias.repository;

import com.elias.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 1:06 下午</p>
 * <p>description: </p>
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    AccessToken findByOwnerIdAndOwnerType(UUID ownerId, Integer ownerType);
}
