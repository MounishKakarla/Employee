package com.employee.model;
//


public class Employee implements Comparable<Employee> {

    private String id;
    private String name;
    private double salary;

    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
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

    public void setSalary(double salary) {
        this.salary = salary;
    }

    
   @Override
    public int compareTo(Employee e) {
        return this.id.compareToIgnoreCase(e.id);
    }

    @Override
    public String toString() {
    	return "id=" + id +
    	           ",name:" + name +
    	           ",salary:" + salary;

    }
}

