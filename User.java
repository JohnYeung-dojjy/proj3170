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
    public User(Connection connection){
        con = connection;
    }
    public void operation(Scanner input_scanner){
        this.DisplayMenu();
        String operation = this.input_scanner.nextLine();
        
        while (! this.ops.contains(operation)){
            System.out.println("invalid operation id! Please input again.");
            System.out.println("Enter Yours Choice: ");
            operation = this.input_scanner.nextLine();
        } 
        switch (operation) {
            case "1":
                // Create all tables
                this.SearchForCars();
            case "2":
                // Delete all tables
                this.ShowUserRentingRecords();
            case "3":
                // Return to main menu
                // equal to doing nothing
            default:
                // if none of the above matches
                // shouldn't be used as it is checked above
                
        }
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


    }

    private void ShowUserRentingRecords(){

    }

}
