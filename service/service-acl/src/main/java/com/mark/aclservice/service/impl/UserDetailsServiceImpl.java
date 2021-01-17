
package com.mark.aclservice.service.impl;

import com.mark.aclservice.entity.User;
import com.mark.aclservice.service.PermissionService;
import com.mark.aclservice.service.UserService;
import com.mark.security.entity.SecurityUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 木可
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        User user = userService.selectByUsername(username);

        // 判断用户是否存在
        if (null == user){
            throw new UsernameNotFoundException("用户名不存在！");
        }
        // 返回UserDetails实现类
        com.mark.security.entity.User curUser = new com.mark.security.entity.User();
        BeanUtils.copyProperties(user,curUser);

        // 构建security的用户实体
        SecurityUser securityUser = new SecurityUser(curUser);

        // 查询用户的权限列表
        List<String> authorities = permissionService.selectPermissionValueByUserId(user.getId());
        // 设置权限列表
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }

}

