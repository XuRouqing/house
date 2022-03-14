package com.example.house.config;

import com.example.house.pojo.User;
import com.example.house.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject= SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        Set<String> roles=new HashSet<String>();
        roles.add("user");
        if ("admin".equals(currentUser.getRole())){
            roles.add("admin");
        }
        if ("designer".equals(currentUser.getRole())){
            roles.add("designer");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken=(UsernamePasswordToken) token;
        User user = userService.findUserByCode(userToken.getUsername());
        if (user==null){
            return null;
        }
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
