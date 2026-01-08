package com.williams;


import java.lang.classfile.instruction.NewMultiArrayInstruction;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        var virtualThreadLocalExample = new VirtualThreadServer();

        virtualThreadLocalExample.run();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}