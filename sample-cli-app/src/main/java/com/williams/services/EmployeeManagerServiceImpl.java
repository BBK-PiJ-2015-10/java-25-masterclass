package com.williams.services;

import com.williams.dtos.Department;
import com.williams.dtos.Employee;

import java.util.*;

public class EmployeeManagerServiceImpl implements EmployeeManagerService {

    private List<Employee> employees = new LinkedList<>();

    @Override
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    @Override
    public List<Employee> listEmployees() {
        return employees;
    }

    @Override
    public List<Employee> searchEmployees(String departmentName) {
        // this .toList returns an immutable list, otherwise collect(Collectors.toList())
        return employees.stream()
                .filter(e -> e.department().name().equals(departmentName)).toList();
    }

    @Override
    public Optional<Double> showAgeStats() {
        return Optional.of(employees.stream().mapToInt(Employee::age).average().getAsDouble());
    }


}
