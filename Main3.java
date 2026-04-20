/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main3;

/**
 *
 * @author musamaluleke
 */
import java.util.Scanner;

public class Main3{

    public static boolean checkUsername(String username) {
        if (username.contains("_") && username.length() <= 5) {
            System.out.println("Username successfully captured.");
            return true;
        } else {
            System.out.println("Username is not correctly formatted.");
            return false;
        }
    }

    public static boolean checkPassword(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        if (password.length() >= 8 && hasCapital && hasNumber && hasSpecial) {
            System.out.println("Password successfully captured.");
            return true;
        } else {
            System.out.println("Password is not correctly formatted.");
            return false;
        }
    }

    public static boolean checkPhoneNumber(String phone) {
        if (phone.startsWith ("0") && phone.length() == 10) {
            System.out.println("Cellphone number successfully added.");
            return true;
        } else {
            System.out.println("Cellphone number incorrectly formatted.");
            return false;
        }
    }

    public static boolean loginUser(String storedUsername, String storedPassword, String storedPhone,
            String inputUsername, String inputPassword, String inputPhone) {

        if (storedUsername.equals(inputUsername) &&
            storedPassword.equals(inputPassword) &&
            storedPhone.equals(inputPhone)) {

            System.out.println("Login successful! Welcome " + storedUsername);
            return true;
        } else {
            System.out.println("Login failed. Details are incorrect.");
            return false;
        }
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("= Register =");

        System.out.print("Enter username: ");
        String username = input.nextLine();
        while (!checkUsername(username)) {
            System.out.print("Try again: ");
            username = input.nextLine();
        }

        System.out.print("Enter password: ");
        String password = input.nextLine();
        while (!checkPassword(password)) {
            System.out.print("Try again: ");
            password = input.nextLine();
        }

        System.out.print("Enter phone number starting with (0): ");
        String phone = input.nextLine();
        while (!checkPhoneNumber(phone)) {
            System.out.print("Try again: ");
            phone = input.nextLine();
        }

        
        System.out.println("\n= Login =");

        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        System.out.print("Enter phone number: ");
        String loginPhone = input.nextLine();

        loginUser(username, password, phone, loginUsername, loginPassword, loginPhone);
    }
}