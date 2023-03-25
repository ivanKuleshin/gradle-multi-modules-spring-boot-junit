package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ServiceClass serviceClass = new ServiceClass("serviceClassMain");
        System.out.println(serviceClass.serviceClassFieldName);
    }
}