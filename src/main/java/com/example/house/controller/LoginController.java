package com.example.house.controller;


import com.example.house.pojo.User;
import com.example.house.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Controller
    @RequestMapping("/user")
    public class UserController {
        @Autowired
        private UserService userService;

        @RequestMapping(value = "/{page}", method = RequestMethod.GET)
        public String showPage(@PathVariable String page) {
            return page;
        }

        @RequestMapping("/session")
        public String testSession(HttpSession session) {
            session.setAttribute("sessionKey", "sessionData--->user");
            return "user";
        }

        @RequestMapping(value = "/logout")
        public String sessionOut(HttpSession session) {
            session.invalidate();
            return "LoginController";
        }

        @PostMapping("/dologin")
        public String dologin(User user, HttpSession session, HttpServletRequest request, Model model) {
            User u = userService.findUserByNameAndPWD(user.getName(), user.getPassword());
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
            try {
                subject.login(token);
                token.setRememberMe(true);
                request.getSession().setAttribute("name", u.getName());
                if (u.getName().equals("admin")) {
                    return "redirect:/admin/main";
                }
                return "redirect:/user/main";
//            if (u.getCode().equals("admin")) {
//                if (userService.login(user.getCode(), user.getPassword())) {
//                    session.setAttribute("userInfo", u);
//                    request.getSession().setAttribute("name", u.getName());
//                    List<User> users = this.userService.getUserList();
//                    model.addAttribute("users", users);
//                    return "redirect:/admin/main";
//                }
//            }
//            if (userService.login(user.getCode(), user.getPassword())) {
//                session.setAttribute("userInfo", u);
//                request.getSession().setAttribute("name", u.getName());
//                List<User> users = this.userService.getUserList();
//                model.addAttribute("users", users);
//                return "redirect:/user/main";
//            }
            } catch (UnknownAccountException e) {
                model.addAttribute("loginErr", "用户名错误");
                return "LoginController";
            } catch (IncorrectCredentialsException e) {
                model.addAttribute("loginErr", "密码错误");
                return "LoginController";
            }
        }
    }
}