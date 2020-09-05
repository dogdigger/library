package com.elias.auth.controller;

import com.elias.auth.form.UserNameAndPasswordLoginForm;
import com.elias.auth.service.AccessTokenService;
import com.elias.auth.view.AccessTokenView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elias.auth.config.PathDefinition;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/5 10:20 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(value = PathDefinition.TOKEN_URI)
public class TokenController {
    private final AccessTokenService tokenService;

    @Autowired
    public TokenController(AccessTokenService tokenService){
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<AccessTokenView> getAccessToken(@RequestBody @Valid UserNameAndPasswordLoginForm loginForm){
        return ResponseEntity.ok(tokenService.getTokenByAccountAndPassword(loginForm));
    }

    @DeleteMapping
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization") UUID token) {
        tokenService.disableToken(token);
        return ResponseEntity.ok("ok");
    }
}
