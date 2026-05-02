package Bank_Mng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/bank";
    private static final String username = "root";
    private static final String password = "#Vansh9402";

    public static void main(String[] args) throws ClassNotFoundException,SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            Account_Manager account_Manager = new Account_Manager(connection, scanner);

            String email;
            long account_no;

            while(true){
                System.out.println("WELLCOME TO BANKING_MANAGEMENT_SYSTEM");
                System.out.println("\n");
                System.out.println("1 : Register");
                System.out.println("2 : Login");
                System.out.println("3 : Exit");
                System.out.print("Eneter you choice : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("user loged in successfully");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.err.println("1 . Open New Bank Account");
                                System.err.println("2 . Exit");
                                int cho = scanner.nextInt();
                                scanner.nextLine();
                                if(cho==1){
                                    account_no = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("you account no is : "+account_no);
                                }else{
                                    break;
                                }
                            }
                            account_no = accounts.get_account_no(email);
                            int choice2 = 0;
                            while (choice2!=5){
                                System.out.println("1 . Debit Money");
                                System.out.println("2 . Credit Money");
                                System.out.println("3 . Transfer Money");
                                System.out.println("4 . Get Balance");
                                System.out.println("5 . Logout");

                                choice2 = scanner.nextInt();
                                scanner.nextLine();
                                switch(choice2){
                                    case 1 :
                                        account_Manager.debit_money(account_no);
                                        break;
                                    case 2 :
                                        account_Manager.creadit_money(account_no);
                                        break;
                                    case 3 :
                                        account_Manager.transfer_money(account_no);
                                        break;
                                    case 4 :
                                        account_Manager.get_balance(account_no);
                                        break;
                                    case 5 :
                                        break;
                                    default :
                                    System.out.println("enter valid choice!!");
                                    break;
                                }
                            }
                        }else{
                            System.out.println("incorrect email or password!!!");
                        }
                        break;

                    case 3 :
                        System.out.println("Thank You For Using Bank_management_System");
                        System.out.println("Existing System");
                        return;
                
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
