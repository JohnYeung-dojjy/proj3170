import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.lang.*;
import java.util.Scanner; // class for user input
import java.util.Set;

import Admin.Admin;
import User.User;
import Manager.Manager;


public class Main {
    private static Scanner input_scanner = new Scanner(System.in);
    private static Set<String> ops = Set.of("1","2","3","4");
    public static void main(String[] args)
    {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db075";
        String dbUsername = "Group12";
        String dbPassword = "love4ever<3";
        
        Connection con = null;
        
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        
                // Do something with the Connection
        
            } catch (ClassNotFoundException e) {
                System.out.println("[Error]: Java MySQL DB Driver not found");
                System.exit(0);
            } 
            catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        Admin admin = new Admin(con);
        Manager manager = new Manager(con);
        User user = new User(con);  
        
        Boolean program_end = false;

        while (!program_end) {
            DisplayMenu();
            String operation = input_scanner.nextLine();

            while (! ops.contains(operation)){
            System.out.println("invalid operation id! Please input again.");
            System.out.println("Enter Yours Choice: ");
            operation = input_scanner.nextLine();
        } 
            switch (operation) {
                case "1":
                    // Admin operation
                    admin.operation();
                case "2":
                    // User operation
                    user.operation();
                case "3":
                    // Manager operation
                    manager.operation();
                case "4":
                    // exit this program
                    program_end = true;
                default:
                    // if none of the above matches
            }
            input_scanner.close(); // close input scanner at the end of program
        }
        

        System.out.println("Thank you for using our Car Renting System!\nGood bye.");
    }
    
    private static void DisplayMenu(){
        System.out.println("Welcome to Car Renting System!\n");
        System.out.println("-----Main menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Operations for Administrator");
        System.out.println("2. Operations for User");
        System.out.println("3. Operations for Manager");
        System.out.println("4. Exit this program");
        System.out.println("Enter Yours Choice: ");
    }
    
}
    