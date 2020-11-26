package com.elias.service;

import com.elias.entity.Client;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/3 8:21 下午</p>
 * <p>description: </p>
 */
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * 根据clientId查找对应的client实体
     *
     * @param clientId clientId
     * @return {@link Client} or {@code null}
     */
    public Client findById(UUID clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }

    /**
     * 校验clientId和secret是否匹配
     *
     * @param clientId clientId
     * @param secret   secret
     */
    public void validateClient(UUID clientId, String secret) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new RestException(ErrorCode.CLIENT_NOT_FOUND);
        }
        if (client.getSecret().equals(secret)) {
            throw new RestException(ErrorCode.WRONG_SECRET_OR_PASSWORD);
        }
    }

}
