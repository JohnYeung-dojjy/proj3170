import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.*;
import java.lang.*;
import java.util.Scanner; // class for user input


public class Main {
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
        Scanner input_scanner = new Scanner(System.in);    
        
        Boolean program_end = false;

        while (!program_end) {
            System.out.println("Welcome to Car Renting System!\n");
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for Administrator");
            System.out.println("2. Operations for User");
            System.out.println("3. Operations for Manager");
            System.out.println("4. Exit this program");
            System.out.println("Enter Yours Choice: ");
            String operation = input_scanner.nextLine();
            while ((operation != "1") || (operation != "2") || (operation != "3") || (operation != "4")){
                System.out.println("invalid operation id! Please input again.");
                System.out.println("Enter Yours Choice: ");
                operation = input_scanner.nextLine();
            } 
            switch (operation) {
                case "1":
                    // Admin operation
                case "2":
                    // User operation
                case "3":
                    // Manager operation
                case "4":
                    // exit this program
                    program_end = true;
                    break;
                default:
                    // if none of the above matches
            }
        }
        

        input_scanner.close(); // close input scanner at the end of program
    } 
    
}
    