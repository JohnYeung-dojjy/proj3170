package User;
import java.sql.Connection;

import java.util.Scanner; // class for user input
import java.util.Set;

public class User {
    // public static void main(String[] args)
    // {
        
    // }
    private Set<String> ops = Set.of("1","2","3");
    private Connection con;
    private Scanner input_scanner = new Scanner(System.in);  
    private String operation; 
    public User(Connection connection){
        con = connection;
    }
    public void operation(){
        this.DisplayMenu();
        operation = this.input_scanner.nextLine();
        
        while (! this.ops.contains(operation)){
            System.out.println("invalid operation id! Please input again.");
            System.out.println("Enter Yours Choice: ");
            operation = this.input_scanner.nextLine();
        } 
        switch (operation) {
            case "1":
                // Search for cars by
                // 1. call number
                // 2. car name
                // 3. company
                this.SearchForCars();
            case "2":
                // Show renting records of a user
                this.ShowUserRentingRecords();
            case "3":
                // Return to main menu
                // equal to doing nothing
            default:
                // if none of the above matches
                // shouldn't be used as it is checked above
                
        }
        this.input_scanner.close();
    }

    private void DisplayMenu(){
        System.out.println("-----Operations for manager menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Search for Cars");
        System.out.println("2. Show loan record of a user");
        System.out.println("3. Return to the main menu");
        System.out.println("Enter Yours Choice: ");
    }

    private void SearchForCars(){
        System.out.println("Choose the search criterion:");
        System.out.println("1. call number");
        System.out.println("2. name");
        System.out.println("3. company");
        System.out.println("Choose the search criterion: ");
        operation = this.input_scanner.nextLine();
        
        while (! this.ops.contains(operation)){
            System.out.println("invalid criterion id! Please input again.");
            System.out.println("Enter Yours Choice: ");
            operation = this.input_scanner.nextLine();
        }
        
        switch (operation){
            case "1":
                this.SearchByCallNumber();
            case "2":
                this.SearchByName();
            case "3":
                this.SearchByCompany();


        }

    }

    private void ShowUserRentingRecords(){

    }

    private void SearchByCallNumber(){

    }

    private void SearchByName(){

    }

    private void SearchByCompany(){

    }

}
