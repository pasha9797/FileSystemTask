package com.practice.security;

import com.practice.model.Permission;
import com.practice.model.User;
import com.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {
    private String USER_ADMIN = "admin";
    private String PASS_ADMIN = "adminpass";

    private String USER = "user";
    private String PASS = "userpass";

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String authentication) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(authentication);
        if (user == null)
            return null;

        List<CustomRole> roleList = new ArrayList<>();
        for (Permission permission : user.getPermissions()) {
            CustomRole role = new CustomRole();
            role.setAuthority(permission.getName());
            roleList.add(role);
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),roleList);
        
        return userDetails;
    }

    private class CustomRole implements GrantedAuthority {
        String role = null;

        @Override
        public String getAuthority() {
            return role;
        }

        public void setAuthority(String roleName) {
            this.role = roleName;
        }

    }
}