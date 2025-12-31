package com.williams;

import com.williams.services.EmployeeManagerServiceImpl;
import com.williams.usecase.EmployeeManagerCLI;

import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        EmployeeManagerCLI app = new EmployeeManagerCLI(
                new EmployeeManagerServiceImpl(), new Scanner(System.in)
        );

        app.start();

    }
}