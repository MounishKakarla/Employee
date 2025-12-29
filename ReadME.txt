‍ Employee Management System (Java + DAO + JSON)
Project Overview

The Employee Management System is a console-based Java application designed to manage employee records efficiently. It follows the DAO (Data Access Object) design pattern and uses JSON files for data persistence instead of a traditional database.

The application supports CRUD operations, role-based access control, and demonstrates serialization and deserialization using Jackson ObjectMapper.


Key Features

 User Login System

Credentials stored in user.json

Re-prompts user on invalid credentials

 Role-Based Access Control

Roles: ADMIN, HR, EMPLOYEE

Permissions managed using enums and maps

JSON-based Persistence

Employee data stored in employees.json

User data stored in user.json

DAO Design Pattern

Clean separation of business logic and data access

CRUD Operations

Add Employee

Update Employee (Full update & Name-only update)

Delete Employee

Fetch All Employees

Fetch by Name

Fetch by Salary

 Custom Exception Handling

Duplicate employee

Employee not found

Data access errors

JUnit 5 Unit Testing

Tests for DAO layer with proper setup and cleanup


run :EmployeeAppMain.java for checking Working Functionality