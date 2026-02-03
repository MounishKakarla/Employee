package com.employee.model;

import com.employee.util.EmailValidator;

public class Employee implements Comparable<Employee> {

    private String id;
    private String name;
    private double salary;
    private String email;
    private String address;
  

    public Employee(String name,double salary) {
        
        this.name = name;
     
        this.salary = salary;
    }
    public Employee(String id,String name,String email,String address,double salary){
    	if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
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
    	if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    	
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
 
    
    public void setEmail(String email) {
    	if(!EmailValidator.isValid(email)) {
    		throw new IllegalArgumentException("Invalid email format");
    	}
        
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
    	this.address=address;
    }
    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    
   @Override
    public int compareTo(Employee emp) {
	   if(emp==null)return 1;
	   if(this.id==null && emp.id==null)return 0;
	   if(this.id ==null)return -1;
	   if(emp.id==null)return 1;
        return this.id.compareToIgnoreCase(emp.id);
    }

   @Override
   public String toString() {
       return id + " | " + name + " | " + email + " | " + address + " | " + salary;
   }


}

