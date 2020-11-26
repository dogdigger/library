package com.elias.service;

import com.elias.entity.Client;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.admin.ClientCreateForm;
import com.elias.model.view.ClientView;
import com.elias.repository.ClientRepository;
import com.elias.util.AppSecretUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author chengrui
 * <p>create at: 2020/11/18 5:12 下午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class AdminService {
    private final ClientRepository clientRepository;

    public AdminService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * 创建一个client，必须要本系统的管理员才能创建
     *
     * @param clientCreateForm {@link ClientCreateForm}
     * @return {@link ClientView}
     */
    public ClientView createClient(ClientCreateForm clientCreateForm) {
        log.info("");
        Client client = clientRepository.findByName(clientCreateForm.getName());
        // 名称已被使用
        if (client != null) {
            throw new RestException(ErrorCode.DUPLICATE_CLIENT_NAME);
        }
        client = new Client();
        BeanUtils.copyProperties(clientCreateForm, client);
        client.setSecret(AppSecretUtils.generate());
        client = clientRepository.save(client);
        ClientView clientView = new ClientView();
        BeanUtils.copyProperties(client, clientView);
        return clientView;
    }
}
