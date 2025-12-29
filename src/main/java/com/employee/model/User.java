package com.employee.model;

import java.util.Set;
import com.employee.security.Role;
import com.employee.security.Permission;

public class User {

    private String username;
    private String password; // encrypted
    private Role role;
     
    

    public User() {}

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public void setRole(Role role) {
    	this.role=role;
    }
    public void setUserName(String username) {
    	this.username=username;
    }

    public void setPassword(String password) { this.password = password; }
    
}
