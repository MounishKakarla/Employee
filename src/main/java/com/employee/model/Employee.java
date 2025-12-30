package com.employee.model;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonSetter;

//


public class Employee implements Comparable<Employee> {

    private String id;
    private String name;
    private double salary;
    private String email;
    private String address;
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@((gmail|hotmail|yahoo)\\.com|company\\.com)$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public Employee(String name,double salary) {
        
        this.name = name;
     
        this.salary = salary;
    }
    public Employee(String id,String name,String email,String address,double salary){
    	this.id=id;
    	this.name=name;
    	this.email=email;
    	this.address=address;
    	this.salary=salary;
    }
    public Employee(String id,String name,double salary) {
    	this.id=id;
    	this.name=name;
    	this.salary=salary;
    }
    public Employee(String name,String email,String address,double salary) {
    	
    	this.name=name;
    	this.email=email;
    	this.address=address;
    	this.salary=salary;
    }
    public Employee() {
    	//Default constructor is mandatory b'coz if we don't have this,Jackson cannot create the objects .without this fetch fails
    }
    
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }
    public String getEmail() {
    	return email;
    }
    public String getAddress() {
    	return address;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
 
    @JsonSetter("email")
    private void jsonSetEmail(String email) {
        // Jackson uses this method ONLY during JSON read
        this.email = email;
    }

    public void setEmail(String email) {
        // Application-level validation (user input)
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(
                "Invalid email. Allowed domains: gmail.com, hotmail.com, yahoo.com, company.com"
            );
        }
        this.email = email;
    }



    public void setAddress(String address) {
    	this.address=address;
    }
    
   @Override
    public int compareTo(Employee e) {
        return this.id.compareToIgnoreCase(e.id);
    }

   @Override
   public String toString() {
       return id + " | " + name + " | " + email + " | " + address + " | " + salary;
   }


}

