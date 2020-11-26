package com.elias.api.auth.client;

import com.elias.config.PathDefinition;
import com.elias.service.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 8:59 下午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.URI_API_AUTH + "/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
}
