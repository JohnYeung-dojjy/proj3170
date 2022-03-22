
package SystemUser;
import java.sql.Connection;

import java.util.Scanner; // class for user input
import java.util.Set;
public class Admin {
    // public static void main(String[] args)
    // {
        
    // }
    private Set<String> ops = Set.of("1","2","3","4","5");
    private Connection con;
    public Scanner input_scanner = new Scanner(System.in);
    private String operation; 
    public Admin(Connection connection){
        con = connection;
    }
    public void operation(){
        Boolean isUsing = true;
        while(isUsing){
            this.DisplayMenu();
            operation = this.input_scanner.nextLine();
            System.out.println("");
            
            while (! this.ops.contains(operation)){
                System.out.println("invalid operation id! Please input again.");
                System.out.print("Enter Yours Choice: ");  
                operation = this.input_scanner.nextLine();
                System.out.println("");
            } 
            switch (operation) {
                case "1":
                    // Create all tables
                    this.CreateTableSchemas();
                    break;
                case "2":
                    // Delete all tables
                    this.DeleteTableSchemas();
                    break;
                case "3":
                    // Load from datafile
                    this.LoadData();
                    break;
                case "4":
                    // Show number of records in each table
                    this.ShowRecordNumber();
                    break;
                case "5":
                    // Return to main menu
                    // equal to doing nothing
                    isUsing = false;
                    break;
                default:
                    // if none of the above matches
                    // shouldn't be used as it is checked above
                    
            }
        }
    }

    private void DisplayMenu(){
        System.out.println("-----Operations for administrator menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Create all tables");
        System.out.println("2. Delete all tables");
        System.out.println("3. Load from datafile");
        System.out.println("4. Show number of records in each table");
        System.out.println("5. Return to the main menu");
        System.out.print("Enter Yours Choice: ");
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
