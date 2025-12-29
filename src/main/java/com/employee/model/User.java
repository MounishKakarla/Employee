package com.employee.model;


import com.employee.security.Role;


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
