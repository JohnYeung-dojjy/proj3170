package SystemUser;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner; // class for user input
import java.util.Set;
import java.io.File;  


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
            String userCategoryQ="CREATE TABLE IF NOT EXISTS user_category(" +
                    "ucid INT(1) NOT NULL," +
                    "max INT(1)  NOT NULL," +
                    "period INT(2) NOT NULL," + 
                    "PRIMARY KEY (ucid)" +
                    ")";	     
                stmt.executeUpdate(userCategoryQ);
            //
            String userQ="CREATE TABLE IF NOT EXISTS user(" +
                "uid VARCHAR(12) NOT NULL, " +
                "name VARCHAR(25) NOT NULL, " +
                "age INT(2) NOT NULL, " + 
                "occupation VARCHAR(20) NOT NULL, " +
                "ucid INT(1) NOT NULL," +
                "PRIMARY KEY (uid)," + 
                "FOREIGN KEY (ucid) REFERENCES user_category(ucid) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(userQ);
            String carCategoryQ="CREATE TABLE IF NOT EXISTS car_category(" +
                "ccid INT(1) NOT NULL," +
                "ccname VARCHAR(20) NOT NULL," +
                "PRIMARY KEY (ccid)" + 
                ")";	     
                stmt.executeUpdate(carCategoryQ);
            String carQ="CREATE TABLE IF NOT EXISTS car(" +
                "callnum VARCHAR(8) NOT NULL, " +
                "name VARCHAR(10) NOT NULL, " +
                "manufacture DATE NOT NULL, " + 
                "time_rent INT(2) NOT NULL, " +
                "ccid INT(1) NOT NULL, " +
                "PRIMARY KEY(callnum), " +
                "FOREIGN KEY (ccid) REFERENCES car_category(ccid) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(carQ);
            String copyQ="CREATE TABLE IF NOT EXISTS copy(" +
                "callnum VARCHAR(8) NOT NULL, " +
                "copynum INT(1) NOT NULL, " +
                "PRIMARY KEY (callnum, copynum) " + 
                // "PRIMARY KEY (callnum, copynum), " + 
                // "FOREIGN KEY (callnum) REFERENCES car(callnum) ON DELETE CASCADE, " +
                // "FOREIGN KEY (copynum) REFERENCES rent(copynum) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(copyQ);
            String rentQ="CREATE TABLE IF NOT EXISTS rent(" +
                "callnum VARCHAR(8) NOT NULL, " +
                "copynum INT(1) NOT NULL, " +
                "uid VARCHAR(12) NOT NULL, " +
                "checkout DATE NOT NULL, " +
                "return_date DATE ," +
                "PRIMARY KEY (uid, callnum, copynum, checkout)," + 
                "FOREIGN KEY (uid) REFERENCES user(uid) ON DELETE CASCADE," +
                "FOREIGN KEY (callnum) REFERENCES car(callnum) ON DELETE CASCADE" +
                ")";	     
                stmt.executeUpdate(rentQ);
            String produceQ="CREATE TABLE IF NOT EXISTS produce(" +
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
            System.out.println("Invalid operation! Exiting to main menu..."); 
            System.out.println();   
            return;
        }
        
    } 
    

    private void DeleteTableSchemas(){
        System.out.printf("Processing...");
        try{
            //initialize Statement
            Statement stmt=con.createStatement();
            //SQL Query
            stmt.executeUpdate("SET foreign_key_checks = 0");
            String deleteTableQuery="DROP TABLE IF EXISTS user_category, " +
                                    "user, " +
                                    "car_category, " +
                                    "car, " +
                                    "copy, " +
                                    "produce, " +
                                    "rent" ;
            
            //Run Query
            stmt.executeUpdate(deleteTableQuery);
            stmt.executeUpdate("SET foreign_key_checks = 1");
            System.out.println("Done. Database is removed.");
        }
        catch (SQLException e){
            System.out.println(e);
            System.out.println("Invalid operation! Exiting to main menu...");
            System.out.println();   
            return;
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
            // ArrayList<String[]> rentData = new ArrayList<>();
            
           
                File filesList[] = dirPath.listFiles();
                
                if(filesList == null){
                    System.out.println("Invalid path! Exiting operation...");
                    // pathReader.close();
                    return;
                }
            
            
            // System.out.println("List of files and directories in the specified directory:");
            
            for(File file : filesList) {
                // System.out.println("File name: "+file.getName());
                // System.out.println("File path: "+file.getAbsolutePath());
                // System.out.println("Size :"+file.getTotalSpace());
                // System.out.println("");
                try{
                    Statement stmt=con.createStatement();
                    File inputFile = new File(path+"/"+file.getName());
                    Scanner lineReader = new Scanner(inputFile);
                    
                    if ( file.getName().equals("user.txt") ){
                        //direct read user
                        // while (lineReader.hasNextLine()) {
                        //     String line = lineReader.nextLine();
                        //     System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     userData.add(lineItems); 
                        //     System.out.println(lineItems);
                        // }
                        stmt.executeUpdate("SET foreign_key_checks = 0");
                        String loadUserQ="LOAD DATA LOCAL INFILE '"+
                                        path+"/"+file.getName()+
                                        "' INTO TABLE user;";	 
                        // System.out.println(loadUserQ); 
                        stmt.executeUpdate(loadUserQ);
                        stmt.executeUpdate("SET foreign_key_checks = 1");
                    }
                    else if ( file.getName().equals("user_category.txt") ){
                        //direct read user_category
                        // System.out.println("B");
                        // while (lineReader.hasNextLine()){
                        //     String line = lineReader.nextLine();
                        //     // System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     usercatData.add(lineItems); 
                        //     // System.out.println(lineItems);
                        // }
                        String loadUserCategoryQ="LOAD DATA LOCAL INFILE '"+
                                                 path+"/"+
                                                 "user_category.txt' INTO TABLE user_category;";	 
                        // System.out.println(loadUserCategoryQ);	     
                        stmt.executeUpdate(loadUserCategoryQ);
            
                    }
                    else if ( file.getName().equals("car.txt") ){
                        // System.out.println("C");
                        while (lineReader.hasNextLine()) {
                            String line = lineReader.nextLine();
                            // System.out.println(line);
                            String[] lineItems = line.split("\t"); 
                            carData.add(lineItems); 
                            // System.out.println(lineItems);
                        }
                    }
                    else if ( file.getName().equals("car_category.txt") ){
                        // System.out.println("D");
                        //direct read car_category
                        // while (lineReader.hasNextLine()) {
                        //     String line = lineReader.nextLine();
                        //     System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     carcatData.add(lineItems); 
                        //     System.out.println(lineItems);
                        // }
                        // //carcat
                        // String carcatInsert = "INSERT INTO car_category (ccid, ccname) VALUES (?,?)";
                        // PreparedStatement carcatPS = con.prepareStatement(carcatInsert);
                        // String ccname=null; 
                        // int ccid = 0;
                        // for (int i = 0; i < carcatData.size(); i++){
                        //     String[] pt = carcatData.get(i);
                        //     for(int j = 0; j < pt.length; j++){
                        //         if (j == 0)
                        //             ccid = Integer.parseInt(pt[j]);
                        //         if (j == 1)
                        //             ccname = pt[j];
                        //         System.out.println("-B-:"+ccid);
                        //         System.out.println("-B-:"+ccname);
                        //     }
                        //     System.out.println("-B-");
                        //     carcatPS.setInt(1,ccid);
                        //     carcatPS.setString(2, ccname);
                        //     carcatPS.executeUpdate();
                        // } 
                        String loadCarCategoryQ="LOAD DATA LOCAL INFILE '"+
                                                path+"/"+
                                                "car_category.txt' INTO TABLE car_category;";     
                        // System.out.println(loadCarCategoryQ);
                        stmt.executeUpdate(loadCarCategoryQ);
                    }
                    else if ( file.getName().equals("rent.txt") ){
                        // System.out.println("E");
                        stmt.executeUpdate("SET foreign_key_checks = 0");
                        String loadRentQ="LOAD DATA LOCAL INFILE '"+
                                                path+"/"+
                                                "rent.txt' INTO TABLE rent;";     
                        // System.out.println(loadRentQ);
                        stmt.executeUpdate(loadRentQ);
                        stmt.executeUpdate("SET foreign_key_checks = 1");
                        // while (lineReader.hasNextLine()) {
                        //     String line = lineReader.nextLine();
                        //     // System.out.println(line);
                        //     String[] lineItems = line.split("\t"); 
                        //     rentData.add(lineItems); 
                        //     // System.out.println(lineItems);
                        // }
                        // for (int i=0; i<2; i++){
                        //     String[] pt = rentData.get(i);
                        //     for(String pp : pt){
                        //         System.out.print(pp);
                        //     }
                        //     // System.out.println("Rent\n");
                        // }
                    }
                    lineReader.close();
                } 
                catch (Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    System.out.println("Invalid operation! Exiting to main menu...");
                    System.out.println();   
                    return;
                }
                
            }
            
        //Write to database for car.txt and rent.txt
        try{
            // String carInsert = "INSERT INTO car VALUES (?,?,?,?,?)";
            // PreparedStatement carPS = con.prepareStatement(carInsert);
            // String rentInsert = "INSERT INTO rent (callnum, copynum, uid, checkout, return_date) " +
            //                     "VALUES (?,?,?,?,?)";
            // PreparedStatement rentPS = con.prepareStatement(rentInsert);
            // String copyInsert = "INSERT INTO copy VALUES (?,?)";
            // PreparedStatement copyPS = con.prepareStatement(copyInsert);
            // String produceInsert = "INSERT INTO produce VALUES (?,?)";
            // PreparedStatement producePS = con.prepareStatement(produceInsert); 
            //backup
            
            String carInsert = "INSERT INTO car (callnum, name, manufacture, time_rent, ccid) VALUES (?,?,?,?,?)";
            PreparedStatement carPS = con.prepareStatement(carInsert);
            // String rentInsert = "INSERT INTO rent (callnum, copynum, uid, checkout, return_date) VALUES (?,?,?,?,?)";
            // PreparedStatement rentPS = con.prepareStatement(rentInsert);
            String copyInsert = "INSERT INTO copy (callnum, copynum) VALUES (?,?)";
            PreparedStatement copyPS = con.prepareStatement(copyInsert);
            String produceInsert = "INSERT INTO produce (cname, callnum) VALUES (?,?)";
            PreparedStatement producePS = con.prepareStatement(produceInsert);                          
            //backup
            //car.txt -> Copy/Produce/Car
            String callnum=null, name=null, companyName=null, manufactureDate =null;
            int numOfCopies = 0, time_rent = 0, ccid = 0;
            
            // System.out.println("Car Data size: "+carData.size());
            for (int i = 0; i < carData.size(); i++){
                String[] pt = carData.get(i);
                callnum         = pt[0];
                numOfCopies     = Integer.parseInt(pt[1]);
                name            = pt[2];
                companyName     = pt[3];
                manufactureDate = pt[4];
                time_rent       = Integer.parseInt(pt[5]);
                ccid            = Integer.parseInt(pt[6]);

                // System.out.println("-A-");
                carPS.setString(1,callnum);
                carPS.setString(2, name);
                carPS.setDate(3, Date.valueOf(manufactureDate));
                carPS.setInt(4, time_rent);
                carPS.setInt(5, ccid);
                carPS.executeUpdate();

                copyPS.setString(1, callnum);
                copyPS.setInt(2, numOfCopies);
                copyPS.executeUpdate();
                
                producePS.setString(1, companyName);
                producePS.setString(2, callnum);
                
                producePS.executeUpdate();

            } 
            
            //rent
            System.out.println("Done. Data is inputted to the database.");

            // String uid, rentDate,returnDate;
            // int copyID;
        }   
        catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.out.println("Invalid operation! Exiting to main menu...");
            System.out.println();   
            return;
        }
            // Statement stmt=con.createStatement();
            // stmt.executeUpdate ( "insert into "+ path +" values (  )" );

        
        
        // pathReader.close();

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
            System.out.println("Invalid operation! Exiting to main menu...");
            System.out.println();   
            return;
        } 
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

