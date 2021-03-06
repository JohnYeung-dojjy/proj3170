package SystemUser;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Set;

public class User {
    private Set<String> ops = Set.of("1","2","3");
    private Connection con;
    //private Scanner input_scanner = new Scanner(System.in);  
    public Scanner input_scanner = new Scanner(System.in);  
    private String operation; 
    public User(Connection connection){
        con = connection;
    }
    public void operation(){
        Boolean isUsing = true;
        while(isUsing){
            this.DisplayMenu();
            operation = this.input_scanner.nextLine();
            System.out.println("");
            // make sure operation id is valid
            while (! this.ops.contains(operation)){
                System.out.println("invalid operation id! Please input again.");
                operation = this.getInput("Enter Yours Choice: ");
            } 
            switch (operation) {
                case "1":
                    // Search for cars by
                    // 1. call number
                    // 2. car name
                    // 3. company
                    this.SearchForCars();
                    break;
                case "2":
                    // Show renting records of a user
                    this.ShowUserRentingRecords();
                    break;
                case "3":
                    // Return to main menu
                    // equal to doing nothing
                    isUsing=false;
                    break;
                default:
                    // if none of the above matches
                    // shouldn't be used as it is checked above
                    break;
            }
        }
    }

    private void DisplayMenu(){
        // System.out.println("-----Operations for manager menu-----");
        System.out.println("-----Operations for user menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Search for Cars");
        System.out.println("2. Show loan record of a user");
        System.out.println("3. Return to the main menu");
        //System.out.println("Enter Yours Choice: ");
        System.out.print("Enter Yours Choice: ");
    }

    private void SearchForCars(){
        System.out.println("Choose the search criterion:");
        System.out.println("1. call number");
        System.out.println("2. name");
        System.out.println("3. company");
        // System.out.println("Choose the search criterion: ");
        System.out.print("Choose the search criterion: ");
        operation = this.input_scanner.nextLine();
        System.out.println("");
        while (! this.ops.contains(operation)){
            System.out.println("invalid criterion id! Exiting operation.");
            // System.out.println("Enter Yours Choice: ");
            //System.out.print("Enter Yours Choice: ");
            //operation = this.input_scanner.nextLine();
            return;
        }
        try{
            switch (operation){
                case "1":
                //call number
                SearchForCarsCallnum();
                break;    
                // end of case 1 
                case "2":
                //by car name
                SearchForCarsname();
                break;    
                //end of case 2
                case "3":
                //by company
                SearchForCarsCompany();
                break;
                //end of case 3
            }// end of switch
        } catch (SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            // System.out.println(ex);
            System.out.println("Car search operation failed.");
        }
            
        // end of SearchForCars
    }

    private void SearchForCarsCallnum() throws SQLException {
        System.out.print("Type in search keyword: ");
        String userinputnum = this.input_scanner.nextLine();
        System.out.println("");
    
        String psql = "SELECT car.callnum, car.name, car_category.ccname, produce.cname, COUNT(*) " +
        "FROM car " +
        "LEFT JOIN car_category ON car.ccid = car_category.ccid " +
        "LEFT JOIN produce ON car.callnum = produce.callnum " +
        "LEFT JOIN copy ON car.callnum = copy.callnum " +
        "WHERE car.callnum = ? "+
        "GROUP BY car.callnum " +
        "ORDER BY car.callnum;";

        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, userinputnum);
        ResultSet resultSet1 = pstmt.executeQuery();

        String psqlSub ="SELECT car.callnum, COUNT(*) " +
        "FROM rent " +
        "LEFT JOIN car on rent.callnum = car.callnum " +
        "WHERE rent.callnum = ? AND rent.return_date=0000-00-00 " +
        "GROUP BY car.callnum ORDER BY car.callnum;";
        PreparedStatement pstmtSub = con.prepareStatement(psqlSub);
        pstmtSub.setString(1, userinputnum);
        ResultSet resultSetsub1 = pstmtSub.executeQuery();

        String[][] occupiedCarNo = new String[100][2];
        int i = 0;
        int j = 0;
        while (resultSetsub1.next()){
            occupiedCarNo[i][0] = resultSetsub1.getString(1);
            occupiedCarNo[i][1] = String.valueOf(resultSetsub1.getInt(2));
            i ++;
        }
        
        if(!resultSet1.isBeforeFirst()){
            System.out.println("No records found.");}
        else if(resultSet1.isBeforeFirst() && occupiedCarNo != null){
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                String carcallnum = resultSet1.getString(1);
                System.out.print("|" + carcallnum);
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                if (carcallnum == occupiedCarNo[j][0]) {
                    int a = resultSet1.getInt(5);
                    int b = Integer.parseInt(occupiedCarNo[j][1]);
                    j++;
                    int noOfAvaliable = a-b;
                    System.out.println("|" + noOfAvaliable + "|");}
                else {System.out.println("|" + resultSet1.getInt(5) + "|");}
            }
        }
        else if (resultSet1.isBeforeFirst())
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                System.out.print("|" + resultSet1.getString(1));
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                System.out.println("|" + resultSet1.getInt(5) + "|");
            }    
        System.out.println("End Of Query");
    }

    private void SearchForCarsname() throws SQLException {
        System.out.print("Type in search keyword: ");
                String userinputcarname = this.input_scanner.nextLine();
                System.out.println("");

                String psql = "SELECT car.callnum, car.name, car_category.ccname, produce.cname, COUNT(*) " +
                "FROM car " + 
                "LEFT JOIN car_category ON car.ccid = car_category.ccid " + 
                "LEFT JOIN produce ON car.callnum = produce.callnum " +
                "LEFT JOIN copy ON car.callnum = copy.callnum " +
                "WHERE car.name LIKE ? " +
                "GROUP BY car.callnum " +
                "ORDER BY car.callnum;";
            
                PreparedStatement pstmt = con.prepareStatement(psql);
                pstmt.setString(1, "%" + userinputcarname + "%");
                ResultSet resultSet1 = pstmt.executeQuery();

                String psqlSub = "SELECT rent.callnum, COUNT(*) " +
                "FROM rent " +
                "LEFT JOIN car on rent.callnum = car.callnum " +
                "WHERE car.name LIKE ? AND rent.return_date IS NULL " +
                "GROUP BY car.name ORDER BY car.callnum;" ;
                
                PreparedStatement pstmtSub = con.prepareStatement(psqlSub);
                pstmtSub.setString(1, "%" + userinputcarname + "%");
                ResultSet resultSetsub1 = pstmtSub.executeQuery();
                String[][] occupiedCarNo = new String[100][2];
                int i = 0;
                int j = 0;
        while (resultSetsub1.next()){
            occupiedCarNo[i][0] = resultSetsub1.getString(1);
            occupiedCarNo[i][1] = String.valueOf(resultSetsub1.getInt(2));
            i++;
        }
        if(!resultSet1.isBeforeFirst())
            System.out.println("No records found.");
        else if(resultSet1.isBeforeFirst() && occupiedCarNo !=null){
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                String carcallnum = resultSet1.getString(1);
                System.out.print("|" + carcallnum);
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                if (carcallnum == occupiedCarNo[j][0] && occupiedCarNo [j][1] != null) {
                    int a = resultSet1.getInt(5);
                    int b = Integer.parseInt(occupiedCarNo[j][1]);
                    j++;
                    int noOfAvaliable = a-b;
                    System.out.println("|" + noOfAvaliable + "|");}
                else {System.out.println("|" + resultSet1.getInt(5) + "|");}
            }
        }
        else if (resultSet1.isBeforeFirst())
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                System.out.print("|" + resultSet1.getString(1));
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                System.out.println("|" + resultSet1.getInt(5) + "|");
            }
        System.out.println("End Of Query\n");
        return;
    }//end of SearchForCarsname

    private void SearchForCarsCompany() throws SQLException {
        System.out.print("Type in search keyword: ");
        String userinputcompany = this.input_scanner.nextLine();
        System.out.println("");
        String psql = "SELECT car.callnum, car.name, car_category.ccname, produce.cname, COUNT(*) AS a " +
        "FROM car " + 
        "LEFT JOIN car_category ON car.ccid = car_category.ccid " +
        "LEFT JOIN produce ON car.callnum = produce.callnum " +
        "LEFT JOIN copy ON car.callnum = copy.callnum " +
        "WHERE produce.cname LIKE ? " +
        "GROUP BY car.callnum " +
        "ORDER BY car.callnum;";

        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, "%" + userinputcompany + "%");
        ResultSet resultSet1 = pstmt.executeQuery();

        String psqlSub = "SELECT rent.callnum, COUNT(*) " +
        "FROM rent " +
        "LEFT JOIN car on rent.callnum = car.callnum " +
        "LEFT JOIN produce ON rent.callnum = produce.callnum " +
        "WHERE produce.cname LIKE ? AND rent.return_date=0000-00-00 " + 
        "GROUP BY rent.callnum ORDER BY rent.callnum; ";

        PreparedStatement pstmtSub = con.prepareStatement(psqlSub);
        pstmtSub.setString(1, "%" + userinputcompany +"%");
        ResultSet resultSetsub1 = pstmtSub.executeQuery();
        
        String[][] occupiedCarNo = new String[100][2];
        int i = 0;
        int j = 0;
        while (resultSetsub1.next()){
            occupiedCarNo[i][0] = resultSetsub1.getString(1);
            occupiedCarNo[i][1] = String.valueOf(resultSetsub1.getInt(2));
            i++;
        }
        
        if(!resultSet1.isBeforeFirst()){
            System.out.println("No records found.");}

        else if(resultSet1.isBeforeFirst() && occupiedCarNo !=null){
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                String carcallnum = resultSet1.getString(1);
                System.out.print("|" + carcallnum);
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                if (carcallnum == occupiedCarNo[j][0]) {
                    int a = resultSet1.getInt(5);
                    int b = Integer.parseInt(occupiedCarNo[j][1]);
                    j++;
                    int noOfAvaliable = a-b;
                    System.out.println("|" + noOfAvaliable + "|");}
                else {System.out.println("|" + resultSet1.getInt(5) + "|");}
            }
        }
        else if (resultSet1.isBeforeFirst())
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while(resultSet1.next()){
                System.out.print("|" + resultSet1.getString(1));
                System.out.print("|" + resultSet1.getString(2));
                System.out.print("|" + resultSet1.getString(3));
                System.out.print("|" + resultSet1.getString(4));
                System.out.println("|" + resultSet1.getInt(5) + "|");
            }
        System.out.println("End Of Query\n");
    }

    private void ShowUserRentingRecords(){
    System.out.println("Enter The user ID: ");
    String userinputID = this.input_scanner.nextLine();
    System.out.println("");
    String sql = null;
    //Statement stmt = con.createStatement();
    //ResultSet resultSet = stmt.executeQuery(sql);
    // while (! this.ops.contains(operation)){
    //     System.out.println("invalid criterion id! Please input again.");
    //     // System.out.println("Enter Yours Choice: ");
    //     //System.out.print("Enter The cuser ID: ");
    //     //userinputID = this.input_scanner.nextLine();
    //     return;
    //     }
    try{
        sql = "SELECT *" +
        "FROM (SELECT DISTINCT rent.callnum, rent.copynum, car.name, produce.cname, rent.checkout, rent.return_date " +
        "FROM user " +
        "JOIN rent ON user.uid = rent.uid " +
        "JOIN car ON rent.callnum = car.callnum " +
        "JOIN produce ON rent.callnum = produce.callnum " +
        "WHERE user.uid = ?) AS Temp ORDER BY Temp.checkout DESC;";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, userinputID);
        ResultSet resultSet = pstmt.executeQuery();
        System.out.println("|CallNum |CopyNum|Name|Company |Check-out |Returned?|");
        while(resultSet.next()){
            System.out.print("|" + resultSet.getString(1));
            System.out.print("|   " + resultSet.getString(2));
            System.out.print("   |" + resultSet.getString(3));
            System.out.print("|" + resultSet.getString(4));
            System.out.print("|" + resultSet.getString(5));
            
            try{
                String dateOfReturn = resultSet.getString(6);
                // System.out.print(dateOfReturn);
                // if (dateOfReturn != "0000-00-00"){
                //     System.out.println("|" + "Yes" + "|");}
                // else {
                //     System.out.println("|" + "No" + "|");}
                // }
                System.out.println("|" + "Yes" + "|");
            }catch(SQLException e){
                System.out.println("|" + "No" + "|");
            }
        }
            
            System.out.println("End Of Query\n");
        } catch( SQLException e) {
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("Showing user renting record failed.\n");
            return;
        }
    } // end of ShowUserRentingRecords


    private String getInput(String display_text){
        // Syntax sugar for better UI representation when getting user input
        String user_input;
        System.out.print(display_text);
        user_input = this.input_scanner.nextLine();
        System.out.println("");
        return user_input;
    }
}