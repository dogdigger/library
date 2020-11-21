package com.elias.api.auth.token;

import com.elias.config.PathDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengrui
 * <p>create at: 2020/11/20 10:51 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.API_AUTH_URI + "/token")
public class TokenController {
}
