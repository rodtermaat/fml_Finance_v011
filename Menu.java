
/**
 * This is the main driver of the application.  
 * It is the UI and eventually will be converted to 
 * a screen and for Android - which started this little adventure.  
 * See the read me for more details
 * 
 * @author (rod termaat)
 * @version (version 0.01 of fml finance)
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

//does the sorting of the arraylist
import java.util.Collections;
import java.util.Comparator;


public class Menu
{
    // instance variables - replace the example below with your own
    Scanner keyboard = new Scanner(System.in);
    boolean exit;   
                    //keeps the program running
    
    int tranID = 0; // created a unique id for each transaction.  I know theres a better way,
                    // but this is what I know now
    boolean zBalance;
                    // tracks if you have initalized the the ledger balance.  
                    // and stops you from doing it more than once. Again better way?
    
    ArrayList<Transaction> rows = new ArrayList<>();
                    // create an ArrayList of the Transaction object and creates
                    // the ledger/checkbook of the application
    Transaction row;
                    // not sure what this actually does, but is needed based
                    // on similar sample code I have studied
                    
    Balance g_balance = new Balance();
                    // created a new Balance object called g_balance
                    // this is the global balance and always to current date
                    // like your atm balance
                    // it is called for all deposits and transactions with dates
                    // of today or previous
    Balance t_balance = new Balance();
                    // this it the transaction balance which includes all transaction
                    // it is used in forecasting and shows the balance into the future
                    // based on ALL (previous, current, and future transactions)
                    
    //Balance balance;
                    // It does not appear that this is needed here.
                    // so why is it needed above in Transaction?                
    
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
    
    // Just the main method that executes when the program starts                
    public static void Main(String[] args){
        Menu menu = new Menu();
        menu.runMenu();
    }

    // drives the program until exit is true which is set in the printMenu method
    public void runMenu(){
        printHeader();
        while(!exit){
            printMenu();
            int choice = getInput();
            performAction(choice);
        }
    }
    
    private void printHeader(){
        System.out.println("---------------------------------");
        System.out.println("|    Welcome to fml Finance.    |");
        System.out.println("|          f ACILITATING        |");
        System.out.println("|          m INIMALIST          |");
        System.out.println("|          l IVING              |");
        System.out.println("---------------------------------");
    }
    
    // Prints out the menu constantly. Almost too much, but hey its a console app
    private void printMenu(){
        System.out.println("");
        System.out.println("");
        System.out.println("   Todays date is..." + sdf.format(cal.getTime()));
        System.out.println("");
        System.out.println("     Please make a selection     ");
        System.out.println("  .............................  ");
        System.out.println("       1) make deposit           ");
        System.out.println("       2) enter expense          ");
        System.out.println("       3) list Transactions      ");
        System.out.println("       4) account setup          ");
        System.out.println("       0) exit                   ");
        System.out.println("");
    
    }
    
    // Reads the input of the user from the printMenu above
    // makes sure their choice is valid
    private int getInput(){
        String response;
        int choice = -1;
        do {
            printResponse("");
            //System.out.println("");
            response = askQuestion("Enter a selection...");
        try {
            choice = Integer.parseInt(response);
        }
        catch(NumberFormatException e){
            printResponse("Error(1): Only numbers allowed");
        }
        if(choice < 0 || choice > 4){
            printResponse("Error(2): 1 - 4 only dumbass");
        }
       } while(choice < 0 || choice > 4);
        return choice;
    
    }
    
    // Attempt to simplify the call and response nature of the console app
    // ask a question to the user and return the reponse string
    private String askQuestion(String question){
        String response = "";
        Scanner input = new Scanner(System.in);
        System.out.print(question);
        response = input.nextLine();
        return response;
    }  
    
    private boolean askYesNo(String question){
        String userInput = "";
        Scanner input = new Scanner(System.in);
        System.out.print(question);
        userInput = input.nextLine();
        if (userInput.equals("y") || userInput.equals("Y")){
            return true;
        }
        else { return false;}
    } 
    
    // generic method to print out a string
    private void printResponse(String response){
        System.out.println("...");
        System.out.println(response);
    }
    
    // Calls the various methods in response to the users choice
    private void performAction(int choice){
        switch(choice){
            case 0:
                //printResponse("");
                printResponse("Thanks for playing. Now go save some $");
                System.exit(0);
                break;
            case 1:
                addDeposit();
                break;
            case 2:
                addExpense();
                break;
            case 3:
                listTransactions();
                break;
            case 4:
                initialBalance();
                break;
             default: 
                printResponse("Error(3) this should not happen - fml");
            }
        }
    
    // Calls the setBalance method in the Balance class to initialize the ledger
    // with an amount the user enters.  Basically your initial deposit into the account
    private void initialBalance(){
        //this process does not stop the user from invoking it after doing some
        //deposits or withdraws.  Need to correct this so they cannot do it later?
        if(!zBalance){
           int iBalance = Integer.parseInt(askQuestion("Enter and initial balance (0 is aok)..."));
           if(iBalance < 0){ 
               iBalance = iBalance * -1;}
           
           g_balance.setBalance(iBalance);
           System.out.println("Account initialize with $" + g_balance.getBalance());
           zBalance = true;
        }
        // prints this if they try to do set up twice.
        else {
            printResponse("Account already seeded, nice try sucker");
        }
    }   
    
    // Calls the deposit method of the Balance class.  It makes sure they
    // enter a number and its accounts for the positive and negative entries.
    // This method and the withdraw method track the balance
    // separately than the transaction.  
    //Something to be considersed later.  Should it all be done together?  
    // Needs to better deal with integer and such, but good for now
    private void addDeposit(){
        int d_amount = 0;
        Date date;
        boolean repeat = false;
           
        try {
          d_amount = Integer.parseInt(askQuestion("Enter deposit amount..."));
        }
        catch(NumberFormatException e) { printResponse("Enter a number silly");
          return;}
          
        if (d_amount < 0){
          d_amount = -1 * d_amount;
        }
        
        String category = askQuestion("Enter category... ");
        String name = askQuestion("Enter description... ");
        
        do {
          String i_date = askQuestion("Enter date like 01/01/18... ");
          String expectedFormat = "MM/dd/yy";
          SimpleDateFormat formatDate = new SimpleDateFormat(expectedFormat);
        
          try
          {
            date = formatDate.parse(i_date);   
          }
          catch(ParseException e) {
            printResponse("I said enter date like 04/15/18 " +
                        "and not some other bogus format");
            return;
          }
        
          addTransaction(date, category, name, d_amount);
          
          // need to olny update the balance for current and previous tranactions
          String display = (g_balance.deposit(d_amount));
          printResponse(display);
          
          repeat = askYesNo("Repeat this transaction?");
        }
          while(repeat);    
}
    
    // Same as deposit, but for Expense
    private void addExpense(){
        int e_amount = 0;
        Date date;
        boolean repeat = false;
        
        try {
          e_amount = Integer.parseInt(askQuestion("Enter expense amount..."));
        }
        catch(NumberFormatException e) { printResponse("Enter a number silly");
          return;}
        
        if (e_amount > 0){
          e_amount = -1 * e_amount;
        }
        
        String category = askQuestion("Enter category... ");
        String name = askQuestion("Enter description... ");
        
        do{
          String i_date = askQuestion("Enter date like 01/01/18... ");
          String expectedFormat = "MM/dd/yy";
          SimpleDateFormat formatDate = new SimpleDateFormat(expectedFormat);
        
          try
          {
            date = formatDate.parse(i_date);   
          }
          catch(ParseException e) {
            printResponse("I said enter date like 04/15/18 " +
                        "and not some other bogus format");
            return;
          }
        
          addTransaction(date, category, name, e_amount);
          String display = g_balance.withdraw(e_amount);
          printResponse(display);
          
          repeat = askYesNo("Repeat this transaction?");
        }
          while(repeat);  
    }
    
    // new transaction method that removes questions within the method
    private void addTransaction(Date date, String category, String name, int t_amount){
        tranID++;
        row = new Transaction(tranID, date, category, name, t_amount);
        rows.add(row);
    }
  
    // List out the rows of the Array list to the console. There is some silly tab (\t) stuff 
    // to try and control the look of the text on the console
    private void listTransactions(){
        System.out.println("----------------------");
        System.out.println("Your balance is...$" + g_balance.getBalance());
        System.out.println("----------------------------------");
        System.out.println("Index\t Date\t\t Category\tName\t\tAmount");
        System.out.println("---------------------------------------------------------------");
        String num;
        
        //sort the ArrayList before printing
        Collections.sort(rows, new Comparator<Transaction>() {
            public int compare(Transaction t1, Transaction t2) {
                return Long.valueOf(t1.getDate().getTime()).compareTo(t2.getDate().getTime());
            }
        });
        
        //loops thru the tranaction object and prints out the rows
        for (Transaction printRow : rows)
        {
            String t_cat, t_name;
            if(printRow.getCategory().length() < 7){
               t_cat = "\t\t";}
               else{ t_cat = "\t";}
            if(printRow.getName().length() < 8) {
                t_name = "\t\t";}
                else{ t_name = "\t";}
                
            //System.out.println(printRow.getName().length());
            System.out.println(
                    printRow.getID() + "\t" + 
                    sdf.format(printRow.getDate()) + "\t " +
                    //printRow.getDate() + "\t " +
                    //printRow.getCategory() + "\t\t" +
                    printRow.getCategory() + t_cat +
                    //printRow.getName() + "\t\t" +
                    printRow.getName() + t_name +
                    printRow.getAmount());

        }
    }
    
}