package SystemUser;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner; // class for user input
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileReader;  
import java.util.ArrayList;

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
    //ch3
    private void CreateTableSchemas(){
        System.out.print("Processing...");
        //initialize Statement
        try{
            Statement stmt=con.createStatement();
            //SQL Query
            //userCategory
            String userCategoryQ="CREATE TABLE user_category(" +
                    "ucid INT(1) NOT NULL," +
                    "max INT(1)  NOT NULL," +
                    "period INT(2) NOT NULL," + 
                    "PRIMARY KEY (ucid)" +
                    ")";	     
                stmt.executeUpdate(userCategoryQ);
            String userQ="CREATE TABLE user(" +
                "uid VARCHAR(12) NOT NULL, " +
                "name VARCHAR(25) NOT NULL, " +
                "age INT(2) NOT NULL, " + 
                "occupation VARCHAR(20) NOT NULL, " +
                "ucid INT(1) NOT NULL," +
                "PRIMARY KEY (uid)" + 
                ")";	     
                stmt.executeUpdate(userQ);
            String carCategoryQ="CREATE TABLE car_category(" +
                "ccid INT(1) NOT NULL," +
                "ccname VARCHAR(20) NOT NULL," +
                "PRIMARY KEY (ccid)" + 
                ")";	     
                stmt.executeUpdate(carCategoryQ);
            String carQ="CREATE TABLE car(" +
                "callnum VARCHAR(8) NOT NULL, " +
                "name VARCHAR(10) NOT NULL, " +
                "manufacture DATE NOT NULL, " + 
                "time_rent DATE NOT NULL, " +
                "ccid INT(1) NOT NULL, " +
                "PRIMARY KEY(), " +
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(carQ);
            String copyQ="CREATE TABLE copy(" +
                "callnum VARCHAR(8) NOT NULL," +
                "copynum INT(1) NOT NULL," +
                "PRIMARY KEY()" +
                ")";	     
                stmt.executeUpdate(copyQ);
            String rentQ="CREATE TABLE rent(" +
                "uid VARCHAR(12) NOT NULL, " +
                "callnum VARCHAR(8) NOT NULL, " +
                "copynum INT(1) NOT NULL, " +
                "checkout DATE NOT NULL, " +
                "return DATE ," +
                "PRIMARY KEY ()," + 
                "FOREIGN KEY () REFERENCES " +
                ")";	     
                stmt.executeUpdate(rentQ);
            String produceQ="CREATE TABLE produce(" +
                "cname VARCHAR(25) NOT NULL, " +
                "callnum VARCHAR(8) NOT NULL, " +
                "PRIMARY KEY (), " + 
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
            String deleteTableQuery="DROP TABLE IF EXISTS user_category, " +
                                    "user, " +
                                    "car_category, " +
                                    "car, " +
                                    "copy, " +
                                    "company, " +
                                    "rent" ;
            
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
        
        //read from wanted file
            // File inputFile = new File(path+".txt");
            // ArrayList<String[]> Data = new ArrayList<>(); 
            // try (BufferedReader TSVReader = new BufferedReader(new FileReader(inputFile))) {
            //     String line = null;
            //     while ((line = TSVReader.readLine()) != null) {
            //         String[] lineItems = line.split("\t"); 
            //         Data.add(lineItems); 
            //         System.out.println(lineItems);
            //     }
            // } catch (Exception e) {
            //     System.out.println("Something went wrong");
            // }
        try{
            ArrayList<String[]> data = this.readDataFile(path);
        }catch(FileNotFoundException e){
            System.out.println("File Not Found");
            pathReader.close();
            return;
        }

        switch (path) {
            case "user_category":
                // some code
                
                break;

            case "user":
                // 
                break;

            case "car_category":
                //
                break;

            case "rent":
                //
                break;

            case "car":
                //
                break;
            default:
                // no use
                break;
            
        }
        //Write to database    
            // Statement stmt=con.createStatement();
            // stmt.executeUpdate ( "insert into "+ path +" values (  )" );

        System.out.println("Done. Data is inputted to the database.");
        pathReader.close();
    }

    private void ShowRecordNumber(){
        System.out.println("Number of records in each table:");
        String[] tableNames = {"user_category", "user", "car_category", "car", "copy", "produce", "rent" };
        int[] noRecord = new int[8];
        try{
            Statement stmt=con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM user_category");
            while (rs1.next()) {
                noRecord[0] = rs1.getInt("counter");
            }
            System.out.println(tableNames[0] + ": " + noRecord[0]);
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM user");
            while (rs2.next()) {
                noRecord[1] = rs2.getInt("counter");
            }
            System.out.println(tableNames[1] + ": " + noRecord[1]);
            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM car_category");
            while (rs3.next()) {
                noRecord[2] = rs3.getInt("counter");
            }
            System.out.println(tableNames[2] + ": " + noRecord[2]);
            ResultSet rs4 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM car");
            while (rs4.next()) {
                noRecord[3] = rs4.getInt("counter");
            }
            System.out.println(tableNames[3] + ": " + noRecord[3]);
            ResultSet rs5 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM copy");
            while (rs5.next()) {
                noRecord[4] = rs5.getInt("counter");
            }
            System.out.println(tableNames[4] + ": " + noRecord[4]);
            ResultSet rs6 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM produce");
            while (rs6.next()) {
                noRecord[5] = rs6.getInt("counter");
            }
            System.out.println(tableNames[5] + ": " + noRecord[5]);
            ResultSet rs7 = stmt.executeQuery("SELECT COUNT(*) AS counter FROM rent");
            while (rs7.next()) {
                noRecord[6] = rs7.getInt("counter");
            }
            System.out.println(tableNames[6] + ": " + noRecord[6]);
            rs1.close();
            rs2.close();
            rs3.close();
            rs4.close();
            rs5.close();
            rs6.close();
            rs7.close();
        }
        
        catch (SQLException e){
            System.out.println(e);
        } 
        System.out.println();
    }

    // see example at the bottom to loop over the returned ArrayList https://www.w3schools.com/java/java_arraylist.asp
    public ArrayList<String[]> readDataFile(String fileName) throws FileNotFoundException{
        
        File DataFile = new File(fileName+".txt");
        Scanner fileReader = new Scanner(DataFile);
        ArrayList<String[]> data = new ArrayList<String[]>();
        while (fileReader.hasNextLine()) {
            String fileData = fileReader.nextLine();
            //System.out.println(data);
            String[] lineItems = fileData.split("\t");
            data.add(lineItems);
        }
        fileReader.close();

        return data;
        
    }

}

// userCategory (ucid, max, period)
// user (uid, name, age, occupation, ucid)
// car_category (ccid, ccname)
// car (callnum, name, manufacture, time_rent, ccid)
// copy (callnum, copynum)
// rent (uid, callnum, copynum, checkout, return)
// produce (cname, callnum)