package com.elias.repository;

import com.elias.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/21 2:17 下午</p>
 * <p>description: </p>
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findById(UUID id);
}
