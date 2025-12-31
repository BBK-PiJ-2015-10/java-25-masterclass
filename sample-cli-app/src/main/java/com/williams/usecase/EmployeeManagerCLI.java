package com.williams.usecase;

import com.williams.dtos.Department;
import com.williams.dtos.Employee;
import com.williams.services.EmployeeManagerService;

import java.util.List;
import java.util.Scanner;

public class EmployeeManagerCLI {

    private EmployeeManagerService employeeManagerService;
    private Scanner scanner;
    private Boolean running = null;

    public EmployeeManagerCLI(EmployeeManagerService employeeManagerService, Scanner scanner) {
        this.employeeManagerService = employeeManagerService;
        this.scanner = scanner;
    }

    public void start() {
        running = true;
        while (running) {
            System.out.println("Enter command (add, list, search, stats, exit)");
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "add" -> addEmployee(scanner);
                case "list" -> listEmployees();
                case "search" -> searchEmployees(scanner);
                case "stats" -> statsOnEmployees();
                case "exit" -> exit();
                default -> System.out.println("Unknown command: " + command);
            }
        }
        if (!running) {
            System.out.println("Good bye");
        }
    }

    private void addEmployee(Scanner scanner) {
        System.out.println("Enter employee name");
        String name = scanner.nextLine().trim();
        System.out.println("Enter employee age");
        int age = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter department name");
        String deptName = scanner.nextLine().trim();
        System.out.println("Enter department location");
        String location = scanner.nextLine().trim();
        Department department = new Department(deptName, location);
        Employee employee = new Employee(name, age, department);
        employeeManagerService.addEmployee(employee);
        System.out.println("Added employee " + employee);
    }

    private void listEmployees() {
        employeeManagerService.listEmployees()
                .forEach(e -> System.out.println(e));

    }

    private void searchEmployees(Scanner scanner) {
        System.out.println("Enter department name");
        String deptName = scanner.nextLine().trim().toLowerCase();
        List<Employee> employees = employeeManagerService.searchEmployees(deptName);
        if (employees.isEmpty()) {
            System.out.println("No employees found under deptName " + deptName);
        } else {
            employees.forEach(e -> System.out.println(e));
        }

    }

    private void statsOnEmployees() {
        var ageStats = employeeManagerService.showAgeStats();
        if (ageStats.isEmpty()) {
            System.out.println("No age stats found");
        } else {
            System.out.println("The avg age is " + ageStats);
        }
    }

    private void exit() {
        running = false;
    }
}
