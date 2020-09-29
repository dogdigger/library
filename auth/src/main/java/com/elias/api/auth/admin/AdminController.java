package com.elias.api.auth.admin;

import com.elias.config.PathDefinition;
import com.elias.form.UserLoginByAccountForm;
import com.elias.form.AppSecretApplyForm;
import com.elias.view.AccessTokenView;
import com.elias.view.AppSecretView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chengrui
 * <p>create at: 2020/9/23 11:43 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.API_AUTH_BASE_URI + "/admin")
public class AdminController {
//    private final AdminService adminService;
//
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    /**
//     * 系统管理员登录
//     * @param userLoginByAccountForm 登录表单
//     * @return 登录结果
//     */
//    @PostMapping("/login")
//    public ResponseEntity<AccessTokenView> login(@RequestBody @Valid UserLoginByAccountForm userLoginByAccountForm) {
//        return ResponseEntity.ok(adminService.adminLogin(userLoginByAccountForm));
//    }
//
//    /**
//     * 系统管理员审核appKey申请
//     * @param applyForm
//     * @return
//     */
//    @PostMapping("/appSecret")
//    public ResponseEntity<AppSecretView> createAppSecret(@RequestBody @Valid AppSecretApplyForm applyForm) {
//        return ResponseEntity.ok(adminService.createAppSecret(applyForm));
//    }
//
//    @GetMapping("/logout")
//    public ResponseEntity<String> logout() {
//        adminService.adminLogout();
//        return ResponseEntity.ok("ok");
//    }
}
