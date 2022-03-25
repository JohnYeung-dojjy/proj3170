
package SystemUser;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
        this.con = connection;
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
        System.out.print("Processing...");
        //initialize Statement
        try{
            Statement stmt=con.createStatement();
            //SQL Query
            //userCategory
            String userCategoryQ="CREATE TABLE userCategory(" +
                    "ucid ," +
                    "max," +
                    "period," + 
                    "PRIMARY KEY (sid, cid)," +
                    "FOREIGN KEY (sid) REFERENCES " +
                    ")";	     
                stmt.executeUpdate(userCategoryQ);
            String userQ="CREATE TABLE user(" +
                "uid, " +
                "name, " +
                "age, " + 
                "occupation, " +
                "ucid," +
                "PRIMARY KEY ()," + 
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(userQ);
            String carCategoryQ="CREATE TABLE carCategory(" +
                "ccid ," +
                "ccname," +
                "PRIMARY KEY ()," + 
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(carCategoryQ);
            String carQ="CREATE TABLE car(" +
                "callnum, " +
                "name, " +
                "manufacture, " + 
                "time_rent, " +
                "ccid, " +
                "PRIMARY KEY(), " +
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(carQ);
            String copyQ="CREATE TABLE copy(" +
                "callnum," +
                "copynum," +
                "PRIMARY KEY()" +
                ")";	     
                stmt.executeUpdate(copyQ);
            String rentQ="CREATE TABLE rent(" +
                "uid, " +
                "callnum, " +
                "copynum, " +
                "checkout, " +
                "return," +
                "PRIMARY KEY ()," + 
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(rentQ);
            String produceQ="CREATE TABLE produce(" +
                "cname ," +
                "callnum," +
                "PRICE varchar(10)," + 
                "PRIMARY KEY ()," + 
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(produceQ);
        }
        catch (SQLException e){
            System.out.println(e);
        }
        System.out.println("Done. Database is initialized.");
    } 
    

    private void DeleteTableSchemas(){
        System.out.printf("Processing...");
        try{
            //initialize Statement
            Statement stmt=con.createStatement();
            //SQL Query
            String deleteTableQuery="DROP TABLE IF EXISTS userCategory, " +
                                    "user, " +
                                    "carCategory, " +
                                    "car, " +
                                    "copy, " +
                                    "rent, " +
                                    "produce" ;
            
            //Run Query
            stmt.executeUpdate(deleteTableQuery);
        }
        catch (SQLException e){
            System.out.println(e);
        } 
        System.out.println("Done. Database is removed.");
        
    }

    private void LoadData(){
        System.out.println();
        System.out.print("Type in the Source Data Folder Path: ");
        String path;
        Scanner pathReader = new Scanner(System.in);
        path = pathReader.nextLine();

        System.out.println("Done. Data is inputted to the database.");
        pathReader.close();
    }

    private void ShowRecordNumber(){
        System.out.println("Number of records in each table:");
        System.out.println();
    }

}

// userCategory (ucid, max, period)
// user (uid, name, age, occupation, ucid)
// car_category (ccid, ccname)
// car (callnum, name, manufacture, time_rent, ccid)
// copy (callnum, copynum)
// rent (uid, callnum, copynum, checkout, return)
// produce (cname, callnum)