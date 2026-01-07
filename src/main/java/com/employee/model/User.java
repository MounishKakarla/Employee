package com.employee.model;

import java.util.HashSet;
import java.util.Set;

import com.employee.security.Role;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String username;
    private String password;
    private String id; 
    
    @JsonAlias("role")
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String username, String id,String password, Set<Role> roles) {
        this.username = username;
        this.id=id;
        this.password = password;
        this.roles = roles;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Set<Role> getRoles() { return roles; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    
    
}
