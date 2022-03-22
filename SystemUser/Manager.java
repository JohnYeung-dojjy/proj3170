package SystemUser;
import java.sql.Connection;

import java.util.Scanner; // class for user input
import java.util.Set;

public class Manager {
    // public static void main(String[] args)
    // {
        
    // }
    private Set<String> ops = Set.of("1","2","3","4");
    private Connection con;
    private Scanner input_scanner = new Scanner(System.in);  
    private String operation;
    public Manager(Connection connection){
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
                // Rent a Car Copy
                this.RentCarCopy();
            case "2":
                // Return a Car
                this.ReturnCar();
            case "3":
                // List unreturned car copies
                this.ListUnreturnedCarCopies();
            case "4":
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
        System.out.println("1. Car Renting");
        System.out.println("2. Car Returning");
        System.out.println("3. List all un-returned car copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.println("Enter Yours Choice: ");
    }

    private void RentCarCopy(){

    }

    private void ReturnCar(){

    }

    private void ListUnreturnedCarCopies(){

    }

}
