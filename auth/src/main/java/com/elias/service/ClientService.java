package com.elias.service;

import com.elias.repository.ClientRepository;
import org.springframework.stereotype.Service;

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

    // 必须要本系统的管理员才能调用
    public void create() {

    }


}
