package com.elain.library.admin.api;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.Request;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author chengrui
 * <p>create at: 2021/1/9 11:48 上午</p>
 * <p>description: </p>
 */
@Controller
@RequestMapping("/api/admin/pages")
public class PagesController {

    @GetMapping("/loginPages")
    public String toLoginPage(
            @RequestParam(name = "redirectURL") String redirectUrl, HttpServletRequest request) {
        request.setAttribute("redirectURL", redirectUrl);
        return "loginPage.html";
    }

    @GetMapping("/redirect")
    public void redirect(@RequestParam(name = "redirectURL", required = false) String redirectUrl,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        System.out.println("session id is: " + session.getId());
        Cookie cookie = new Cookie("readOnlyCookie", "JSESSIONID=" + session.getId());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        String requestURL = request.getRequestURL().toString();
        System.out.println("requestURL is: " + requestURL);
        if (!StringUtils.isEmpty(redirectUrl)) {
            redirectUrl += "?JSESSIONID=" + session.getId();
        }
        response.sendRedirect(redirectUrl);
    }

}
