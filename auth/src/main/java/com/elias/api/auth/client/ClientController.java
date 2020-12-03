package com.elias.api.auth.client;

import com.elias.common.PathDefinition;
import com.elias.service.ClientService;
import com.elias.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 8:59 下午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.URI_API_CLIENT)
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;

    public ClientController(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }
}
