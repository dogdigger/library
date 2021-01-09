package com.elias.api.auth;

import com.elias.common.Constants;
import com.elias.common.PathDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chengrui
 * <p>create at: 2021/1/9 11:27 上午</p>
 * <p>description: 普通controller，主要提供一些页面</p>
 */
@Controller
@RequestMapping(PathDefinition.URI_API_AUTH + "/pages")
public class SystemController {

    @GetMapping("/login")
    public String toLoginPage() {
        return "loginPage";
    }
}
