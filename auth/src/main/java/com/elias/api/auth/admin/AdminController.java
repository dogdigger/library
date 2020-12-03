package com.elias.api.auth.admin;

import com.elias.common.PathDefinition;
import com.elias.model.form.admin.ClientCreateForm;
import com.elias.model.view.ClientView;
import com.elias.response.GenericResponse;
import com.elias.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author chengrui
 * <p>create at: 2020/9/23 11:43 上午</p>
 * <p>description: </p>
 */
@Controller
@RequestMapping(PathDefinition.URI_API_AUTH)
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/client/actions/create")
    public GenericResponse<ClientView> createClient(@RequestBody @Valid ClientCreateForm clientCreateForm) {
        return new GenericResponse<>(adminService.createClient(clientCreateForm));
    }

    @GetMapping("/test")
    public ResponseEntity<String> redirect(){
        return ResponseEntity.ok("hello, redirect");
    }

    @GetMapping("/redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://www.baidu.com");
    }
    @GetMapping("/redirect")
    public String testRedirect() {
        return "redirect:http://www.baidu.com";
    }
}
