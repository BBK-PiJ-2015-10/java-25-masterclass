package com.williams.services;

import com.williams.dtos.Department;
import com.williams.dtos.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeManagerService {

    void addEmployee(Employee employee);

    List<Employee> listEmployees();

    List<Employee> searchEmployees(String departmentName);

    Optional<Double> showAgeStats();

}
