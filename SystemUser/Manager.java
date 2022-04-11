package SystemUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner; // class for user input
import java.util.Set;

import java.text.SimpleDateFormat;
// import java.util.Date;
import java.text.ParseException;

public class Manager {
    private Set<String> ops = Set.of("1","2","3","4"); // a set of all valid operation id
    private Connection con;
    public Scanner input_scanner = new Scanner(System.in);  
    private String operation; 
    public Manager(Connection connection){
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
                    // Rent a Car Copy
                    this.RentCarCopy();
                    break;
                case "2":
                    // Return a Car
                    this.ReturnCar();
                    break;
                case "3":
                    // List unreturned car copies
                    this.ListUnreturnedCarCopies();
                    break;
                case "4":
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
        System.out.println("-----Operations for manager menu-----");
        System.out.println("What kinds of operations would you like to perform?");
        System.out.println("1. Car Renting");
        System.out.println("2. Car Returning");
        System.out.println("3. List all un-returned car copies which are checked-out within a period");
        System.out.println("4. Return to the main menu");
        System.out.print("Enter Yours Choice: ");
    }

    private void RentCarCopy(){
        PreparedStatement st;
        ResultSet rs = null;
        String UserID = this.getInput("Enter The User ID: ");
        try{
            st = con.prepareStatement("SELECT * FROM user WHERE uid=?");
            st.setString(1, UserID);
            rs = st.executeQuery();
            if(!rs.next()){
                System.out.println("User not exist! Exiting operation...");
                return;
            }
        }catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("exiting operation...");
            return;
        }

        String CallNum = this.getInput("Enter The Call Number: ");
        String CopyNum = this.getInput("Enter The Copy Number: ");
        try{
            st = con.prepareStatement("SELECT * FROM copy WHERE callnum=? AND copynum=?");
            st.setString(1, CallNum);
            st.setString(2, CopyNum);
            rs = st.executeQuery();
            if(!rs.next()){
                System.out.println("Car copy not exist! Exiting operation...\n");
                return;
            }
        }catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("Car renting failed.\n");
            return;

        }
        try{
            st = con.prepareStatement("SELECT * FROM rent WHERE uid=? AND callnum=? AND copynum=? AND `return_date`=0000-00-00");
            st.setString(1, UserID);
            st.setString(2, CallNum);
            st.setString(3, CopyNum);
            rs = st.executeQuery();
            
            if(!rs.next()){// if there is no rent record of the specified car copy with NULL return date
                // borrow the car copy
                // insert record into rent
                // uid=UserID, callnum=CallNum, copynum=CopyNum, checkout=Date, return=NULL
                st = con.prepareStatement("INSERT INTO rent(uid, callnum, copynum, checkout, return_date) VALUES (?, ?, ?, ?, 0000-00-00)");

                st.setString(1, UserID);
                st.setString(2, CallNum);
                st.setString(3, CopyNum);
                java.util.Date today = new java.util.Date(); // the day of renting
                st.setDate(4, new Date(today.getTime()));

                st.executeUpdate();
                
            }
            System.out.println("Car renting performed successfully.\n");
            // System.out.println("Rented car information:");
            // System.out.println("");
        
        }catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("Car renting failed.\n");
            return;
        }
    }

    private void ReturnCar(){
        PreparedStatement st;
        ResultSet rs = null;
        String UserID = this.getInput("Enter The User ID: ");
        try{
            st = con.prepareStatement("SELECT * FROM user WHERE uid=?");
            st.setString(1, UserID);
            rs = st.executeQuery();
            if(!rs.next()){
                System.out.println("User not exist! Exiting operation...");
                return;
            }
        }catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("exiting operation...");
            return;
        }

        String CallNum = this.getInput("Enter The Call Number: ");
        String CopyNum = this.getInput("Enter The Copy Number: ");
        try{
            st = con.prepareStatement("SELECT * FROM `rent` WHERE `uid`=? AND `callnum`=? AND `copynum`=? AND `return_date`=0000-00-00"); // get the rent record where the car is still being rented
            st.setString(1, UserID);
            st.setString(2, CallNum);
            st.setString(3, CopyNum);
            rs = st.executeQuery();
            if(rs.next()){
                st = con.prepareStatement("UPDATE `rent` SET `return_date`=? WHERE `uid`=? AND `callnum`=? AND `copynum`=? AND `return_date`=0000-00-00"); // update that rent record
                java.util.Date today = new java.util.Date(); // the day of returning
                st.setDate(1, new Date(today.getTime()));
                st.setString(2, UserID);
                st.setString(3, CallNum);
                st.setString(4, CopyNum);
                int update_result = st.executeUpdate();

                System.out.println("Car returning performed successfully\n");
            }else{
                System.out.println("Record not exist! Exiting operation...");
                System.out.println("Car returning failed.\n");
                return;
            }
        }catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            System.out.println("Car returning failed.\n");
            return;

        }
    }

    private void ListUnreturnedCarCopies(){
        Statement stmt = null;
        PreparedStatement st;
        ResultSet rs = null;
        String d1="dd/mm/yyyy", d2="dd/mm/yyyy"; //date_1, date_2
        // Boolean d1_ok=false, d2_ok=false;

        java.util.Date util_d1 = new java.util.Date();
        java.util.Date util_d2 = new java.util.Date();
        
        /*
        * Set preferred date format,
        * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);  // range of day/month/year must be valid https://blog.csdn.net/KingJin_CSDN_/article/details/53893210
        /* Create Date object
        * parse the string into date 
        */
        // check if both dates input are valid, if user input 'return', operation will be cancelled and return to manager menu

        d1 = this.getInput("Type in the starting date [dd/mm/yyyy]: ");

        // check if input date is in valid format
        try{
            util_d1 = sdfrmt.parse(d1); // check if d1 is in correct format, if not, ParseException is thrown
            // d1_ok = true;   // break the loop
        }catch (ParseException e){
            System.out.println(d1+" is Invalid Date format! Exiting operation...");
            return;
        }

        d2 = this.getInput("Type in the ending date [dd/mm/yyyy]: ");
        // check if input date is in valid format
        try{
            util_d2 = sdfrmt.parse(d2); // check if d2 is in correct format, if not, ParseException is thrown
            // d2_ok = true;   // break the loop
        }catch (ParseException e){
            System.out.println(d2+" is Invalid Date format! Exiting operation...");
            return;
        }

        if (util_d2.getTime() < util_d1.getTime()){
            System.out.println("ending date cannot be before starting date");
            return;
        }

            
        try{
            
            // select rent records where cars hasn't been returned, and was rented between the range
            st = con.prepareStatement("SELECT * FROM rent WHERE `return_date`=0000-00-00 AND `checkout` BETWEEN ? and ? ORDER BY checkout DESC");
            Date sql_d1 = new Date(util_d1.getTime()); // change date from input format into sql format
            Date sql_d2 = new Date(util_d2.getTime()); // change date from input format into sql format
            st.setDate(1, sql_d1);  // set first  ? in st to sql_d1
            st.setDate(2, sql_d2);  // set second ? in st to sql_d2
            rs = st.executeQuery();

            // print out all unreturned cars
            System.out.println("List of UnReturned Cars");
            System.out.println("|    UID     |CallNum |CopyNum| Checkout |");
            while(rs.next()){ // print result of query one by one
                String uid     = rs.getString("uid");
                String callnum = rs.getString("callnum");
                String copynum = rs.getString("copynum");
                String checkout_date = rs.getString("checkout");
                System.out.println("|"+uid+"|"+callnum+"|   "+copynum+"   |"+checkout_date);
                
            }
            System.out.println("\nEnd of Query\n");
        }
        catch(SQLException e){
            System.out.println("something went wrong: " + e.getMessage());
            // System.out.println(ex);
            System.out.println("Car listing operation failed.");
        }
    }

    // from https://beginnersbook.com/2013/05/java-date-format-validation/
    private boolean validateDate(String strDate)
    {
        /*
        * Set preferred date format,
        * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);  // range of day/month/year must be valid https://blog.csdn.net/KingJin_CSDN_/article/details/53893210
        /* Create Date object
        * parse the string into date 
            */
        try
        {
            java.util.Date javaDate = sdfrmt.parse(strDate); 
            // System.out.println(strDate+" is valid date format");
        }
        /* Date format is invalid */
        catch (ParseException e)
        {
            // System.out.println(strDate+" is Invalid Date format");
            return false;
        }
        /* Return true if date format is valid */
        return true;

    }
    private String getInput(String display_text){
        // Syntax sugar for better UI representation when getting user input
        String user_input;
        System.out.print(display_text);
        user_input = this.input_scanner.nextLine();
        System.out.println("");
        return user_input;
    }

}
