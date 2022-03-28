package SystemUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
// import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner; // class for user input
import java.util.Set;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    }

    private void ReturnCar(){

    }

    private void ListUnreturnedCarCopies(){
        Statement stmt = null;
        PreparedStatement st;
        ResultSet rs = null;
        String d1="dd/mm/yyyy", d2="dd/mm/yyyy"; //date_1, date_2
        Boolean d1_ok=false, d2_ok=false;
        // check if both dates input are valid, if user input 'return', operation will be cancelled and return to manager menu
        while (!d1_ok){
            d1 = this.getInput("Type in the starting date [dd/mm/yyyy] [type 'return' to cancel operation]: ");

            // if user input 'return', operation will be cancelled and return to Manager menu
            if (d1 =="return"){
                return;
            }

            // check if input date is in valid format
            if (validateJavaDate(d1)){
                d1_ok = true;
            }else{
                System.out.println(d1+" is Invalid Date format, please try again!");
            }
        }
        while (!d2_ok){
            d2 = this.getInput("Type in the ending date [dd/mm/yyyy]  [type 'return' to cancel operation]: ");

            // if user input 'return', operation will be cancelled and return to Manager menu
            if (d2 =="return"){
                return;
            }

            // check if input date is in valid format
            if (validateJavaDate(d2)){
                d2_ok = true;
            }else{
                System.out.println(d2+" is Invalid Date format, please try again!");
            }
        }
            
        try{
            System.out.println("List of UnReturned Cars");
            System.out.println("|    UID     |CallNum |CopyNum| Checkout  |");
            // stmt = con.createStatement();
            // rs = stmt.executeQuery("SELECT * FROM `rent` WHERE `return`=NULL;");
            st = con.prepareStatement("SELECT * FROM rent WHERE return=NULL AND `checkout` BETWEEN ? and ? ORDER BY checkout ASC");
            st.setString(1, d1);
            st.setString(2, d2);
            rs = st.executeQuery();
            while(rs.next()){
                String uid     = rs.getString("uid");
                String callnum = rs.getString("callnum");
                String copynum = rs.getString("copynum");
                String checkout_date = rs.getString("checkout");
                System.out.println("|"+uid+"|"+callnum+"|   "+copynum+"   |"+checkout_date);
                
            }
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        finally{
            System.out.println("End of Query");
        }
    }

    // from https://beginnersbook.com/2013/05/java-date-format-validation/
    private boolean validateJavaDate(String strDate)
    {
        /* Check if date is 'null' */
        if (strDate.trim().equals(""))
        {
            return false; // null is not accepted
        }
        /* Date is not 'null' */
        else
        {
            /*
            * Set preferred date format,
            * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            /* Create Date object
            * parse the string into date 
                */
            try
            {
                Date javaDate = sdfrmt.parse(strDate); 
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
    }
    private String getInput(String display_text){
        String user_input;
        System.out.print(display_text);
        user_input = this.input_scanner.nextLine();
        System.out.println("");
        return user_input;
    }

}
