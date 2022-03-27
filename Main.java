
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.lang.*;
import java.util.Scanner; // class for user input
import java.util.Set;

import SystemUser.*;


public class Main {
    private static Scanner input_scanner = new Scanner(System.in);
    private static Set<String> ops = Set.of("1","2","3","4");
    public static void main(String[] args)
    {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db12";
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
        
        System.out.println("Welcome to Car Renting System!\n");
        while (!program_end) {
            DisplayMenu();
            String operation = input_scanner.nextLine();
            System.out.println("");

            while (! ops.contains(operation)){
            System.out.println("invalid operation id! Please input again.");
            System.out.print("Enter Yours Choice: ");
            operation = input_scanner.nextLine();
            System.out.println("");
        } 
            switch (operation) {
                case "1":
                    // Admin operation
                    admin.operation();
                    break;
                case "2":
                    // User operation
                    user.operation();
                    break;
                case "3":
                    // Manager operation
                    manager.operation();
                    break;
                case "4":
                    // exit this program
                    program_end = true;
                    break;
                default:
                    // if none of the above matches
            }
        }
        
        input_scanner.close(); // close input scanner at the end of program
        admin.input_scanner.close(); // close admin input scanner
        user.input_scanner.close(); // close user input scanner
        manager.input_scanner.close(); // close manager input scanner
        

        System.out.println("Thank you for using our Car Renting System!\nGood bye.");
    }
    
    private static void DisplayMenu(){
        System.out.println("-----Main menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Operations for Administrator");
        System.out.println("2. Operations for User");
        System.out.println("3. Operations for Manager");
        System.out.println("4. Exit this program");
        System.out.print("Enter Yours Choice: ");
    }
    
}
    