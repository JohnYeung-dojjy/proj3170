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
    // private String operation; 
    public Admin(Connection connection){
        this.con = connection;
    }
    public void operation(){
        Boolean isUsing = true;
        while(isUsing){
            this.DisplayMenu();
            String operation = this.input_scanner.nextLine();
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
                    return;
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
            //ok
            String userCategoryQ="CREATE TABLE user_category(" +
                    "ucid INT(1) NOT NULL," +
                    "max INT(1)  NOT NULL," +
                    "period INT(2) NOT NULL," + 
                    "PRIMARY KEY (ucid)" +
                    ")";	     
                stmt.executeUpdate(userCategoryQ);
            //
            String userQ="CREATE TABLE user(" +
                "uid VARCHAR(12) NOT NULL, " +
                "name VARCHAR(25) NOT NULL, " +
                "age INT(2) NOT NULL, " + 
                "occupation VARCHAR(20) NOT NULL, " +
                "ucid INT(1) NOT NULL," +
                "PRIMARY KEY (uid)," + 
                "FOREIGN KEY (ucid) REFERENCES user_category(ucid) ON DELETE CASCADE" +
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
                "PRIMARY KEY(callnum), " +
                "FOREIGN KEY (ccid) REFERENCES car_category(ccid) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(carQ);
            String copyQ="CREATE TABLE copy(" +
                "callnum VARCHAR(8) NOT NULL," +
                "copynum INT(1) NOT NULL," +
                "PRIMARY KEY(callnum, copynum)," +
                "FOREIGN KEY (copynum) REFERENCES rent(copynum) ON DELETE CASCADE," +
                "FOREIGN KEY (callnum) REFERENCES car(callnum) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(copyQ);
            String rentQ="CREATE TABLE rent(" +
                "callnum VARCHAR(8) NOT NULL, " +
                "copynum INT(1) NOT NULL, " +
                "uid VARCHAR(12) NOT NULL, " +
                "checkout DATE NOT NULL, " +
                "return DATE ," +
                "PRIMARY KEY (uid, callnum, copynum, checkout)," + 
                "FOREIGN KEY (uid) REFERENCES user(uid) ON DELETE CASCADE," +
                "FOREIGN KEY (callnum) REFERENCES car(callnum) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(rentQ);
            String produceQ="CREATE TABLE produce(" +
                "cname VARCHAR(25) NOT NULL, " +
                "callnum VARCHAR(8) NOT NULL, " +
                "PRIMARY KEY (cname, callnum), " + 
                "FOREIGN KEY (callnum) REFERENCES car(callnum) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(produceQ);
            System.out.println("Done. Database is initialized.");
        }
        catch (SQLException e){
            System.out.println(e);
        }
        
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
            System.out.println("Done. Database is removed.");
        }
        catch (SQLException e){
            System.out.println(e);
        } 
        
        
    }

    private void LoadData(){
        System.out.print("Type in the Source Data Folder Path: ");
        String path;
        Scanner pathReader = new Scanner(System.in);
        path = pathReader.nextLine();
        
        //read from wanted file
            //Creating a File object for directory
            File dirPath = new File("./"+path+"/");
            //List of all files and directories
            ArrayList<String[]> carData = new ArrayList<>();
            // ArrayList<String[]> carcatData = new ArrayList<>();
            // ArrayList<String[]> userData = new ArrayList<>();
            // ArrayList<String[]> usercatData = new ArrayList<>();
            ArrayList<String[]> rentData = new ArrayList<>();
            
           
                File filesList[] = dirPath.listFiles();
                
                if(filesList == null){
                    System.out.println("Invalid path! Exiting operation...");
                    pathReader.close();
                    return;
                }
            
            
            System.out.println("List of files and directories in the specified directory:");
            
            for(File file : filesList) {
                System.out.println("File name: "+file.getName());
                System.out.println("File path: "+file.getAbsolutePath());
                System.out.println("Size :"+file.getTotalSpace());
                System.out.println("");
                try{
                    Statement stmt=con.createStatement();
                    File inputFile = new File("./"+path+"/"+file.getName());
                    Scanner lineReader = new Scanner(inputFile);
                    
                    if ( file.getName().equals("user.txt") ){
                        //direct read user
                        // while (lineReader.hasNextLine()) {
                        //     String line = lineReader.nextLine();
                        //     // System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     userData.add(lineItems); 
                        //     // System.out.println(lineItems);
                        // }
                        String loadUserQ="LOAD DATA INFILE '"+
                                        "./"+path+"/"+
                                        "user.txt' INTO TABLE user;";	 
                        System.out.println(loadUserQ); 
                        stmt.executeUpdate(loadUserQ);
                    }
                    else if ( file.getName().equals("user_category.txt") ){
                        //direct read user_category
                        System.out.println("B");
                        // while (lineReader.hasNextLine()){
                        //     String line = lineReader.nextLine();
                        //     // System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     usercatData.add(lineItems); 
                        //     // System.out.println(lineItems);
                        // }
                        String loadUserCategoryQ="LOAD DATA INFILE '"+
                                                "./"+path+"/"+
                                                "user_category.txt' INTO TABLE user_category;";	 
                        System.out.println(loadUserCategoryQ);	     
                        stmt.executeUpdate(loadUserCategoryQ);
            
                    }
                    else if ( file.getName().equals("car.txt") ){
                        System.out.println("C");
                        while (lineReader.hasNextLine()) {
                            String line = lineReader.nextLine();
                            // System.out.println(line);
                            String[] lineItems = line.split("\t"); 
                            carData.add(lineItems); 
                            // System.out.println(lineItems);
                        }
                    }
                    else if ( file.getName().equals("car_category.txt") ){
                        System.out.println("D");
                        //direct read car_category
                        // while (lineReader.hasNextLine()) {
                        //     String line = lineReader.nextLine();
                        //     // System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     carcatData.add(lineItems); 
                        //     // System.out.println(lineItems);
                        // }
                        String loadCarCategoryQ="LOAD DATA INFILE '"+
                                                "./"+path+"/"+
                                                "car_category.txt' INTO TABLE car_category;";     
                        stmt.executeUpdate(loadCarCategoryQ);
                    }
                    else if ( file.getName().equals("rent.txt") ){
                        System.out.println("E");
                        while (lineReader.hasNextLine()) {
                            String line = lineReader.nextLine();
                            // System.out.println(line);
                            String[] lineItems = line.split("\t"); 
                            rentData.add(lineItems); 
                            // System.out.println(lineItems);
                        }
                        for (int i=0; i<2; i++){
                            String[] pt = rentData.get(i);
                            for(String pp : pt){
                                System.out.print(pp);
                            }
                            System.out.println(" A");
                        }
                    }
                    lineReader.close();
                } 
                catch (Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                
            }
            
        //Write to database for car.txt and rent.txt
        try{
            
                
        }   
        catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
    // public ArrayList<String[]> readDataFile(String fileName) throws FileNotFoundException{
        
    //     File DataFile = new File(fileName+".txt");
    //     Scanner fileReader = new Scanner(DataFile);
    //     ArrayList<String[]> data = new ArrayList<String[]>();
    //     while (fileReader.hasNextLine()) {
    //         String fileData = fileReader.nextLine();
    //         //System.out.println(data);
    //         String[] lineItems = fileData.split("\t");
    //         data.add(lineItems);
    //     }
    //     fileReader.close();

    //     return data;
        
    // }

    // public void loadUserCategory(ArrayList<String[]> data){
    //     for (String[] item: data){
    //         String ucid = item[0];
    //         String max = item[1];
    //         String period = item[2];
    //         // insert to db
    //     }
    // }

    // public void loadUser(ArrayList<String[]> data){
        
    // }

    // public void loadCarCategory(ArrayList<String[]> data){
        
    // }

    // public void loadRent(ArrayList<String[]> data){

    // }

    // public void loadCar(ArrayList<String[]> data){
        
    // }

    

}

// userCategory (ucid, max, period)
// user (uid, name, age, occupation, ucid)
// car_category (ccid, ccname)
// car (callnum, name, manufacture, time_rent, ccid)
// copy (callnum, copynum)
// rent (uid, callnum, copynum, checkout, return)
// produce (cname, callnum)

