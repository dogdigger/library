package com.elias.repository;

import com.elias.entity.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/12/14 5:40 下午</p>
 * <p>description: </p>
 */
public interface ClientUserRepository extends JpaRepository<ClientUser, UUID> {
    ClientUser findByClientIdAndUserId(UUID clientId, UUID userId);
}
