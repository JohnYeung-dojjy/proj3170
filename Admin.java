import java.sql.Connection;

import java.util.Scanner; // class for user input
import java.util.Set;
public class Admin {
    // public static void main(String[] args)
    // {
        
    // }
    private Set<String> ops = Set.of("1","2","3","4","5");
    private Connection con;
    private Scanner input_scanner = new Scanner(System.in);    
    public Admin(Connection connection){
        con = connection;
    }
    public void operation(Scanner input_scanner){
        this.DisplayMenu();
        String operation = input_scanner.nextLine();
        
        while (! this.ops.contains(operation)){
            System.out.println("invalid operation id! Please input again.");
            System.out.println("Enter Yours Choice: ");  
            operation = this.input_scanner.nextLine();
        } 
        switch (operation) {
            case "1":
                // Create all tables
                this.CreateTableSchemas();
            case "2":
                // Delete all tables
                this.DeleteTableSchemas();
            case "3":
                // Load from datafile
                this.LoadData();
            case "4":
                // Show number of records in each table
                this.ShowRecordNumber();
            case "5":
                // Return to main menu
                // equal to doing nothing
            default:
                // if none of the above matches
                // shouldn't be used as it is checked above
                
        }
        this.input_scanner.close();
    }

    private void DisplayMenu(){
        System.out.println("-----Operations for administrator menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        System.out.println("Enter Yours Choice: ");
    }
    private void CreateTableSchemas(){

    }

    private void DeleteTableSchemas(){

    }

    private void LoadData(){

    }

    private void ShowRecordNumber(){

    }

}
