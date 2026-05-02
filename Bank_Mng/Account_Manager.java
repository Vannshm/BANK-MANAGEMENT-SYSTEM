package Bank_Mng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Scanner;

public class Account_Manager {
    private Connection connection;
    private Scanner scanner;

    public Account_Manager(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debit_money(long acc_no) throws SQLDataException{
        System.out.println("enter the ammount : ");
        double ammount = scanner.nextDouble();
        scanner.nextLine();
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(acc_no!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts where account_no = ? AND security_key = ?");
                preparedStatement.setLong(1, acc_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_money = resultSet.getDouble("balance");
                    if(ammount<=current_money){
                        String debit_query = "UPDATE accounts SET balance = balance-? WHERE account_no = ? ";
                        PreparedStatement preparedStatement2 = connection.prepareStatement(debit_query);
                        preparedStatement2.setDouble(1, ammount);
                        preparedStatement2.setLong(2, acc_no);

                        int affectRow = preparedStatement2.executeUpdate();

                        if(affectRow>0){
                            System.out.println("RS "+ammount+" debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("transition failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("insufficient balance!!!");
                    }
                }else{
                    System.out.println("Wrong pin!!");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        // connection.setAutoCommit(true);
    }

    // ***********************************************************************************

    public void creadit_money(long account_no){
        System.out.println("enter the ammount : ");
        double ammount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("enter your security_pin : ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(account_no!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_no = ? AND security_key = ?");
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String credit_query = "UPDATE accounts SET balance = balance+? WHERE account_no = ?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(credit_query);
                    preparedStatement2.setDouble(1, ammount);
                    preparedStatement2.setLong(2, account_no);
                    int affected_row = preparedStatement2.executeUpdate();

                    if(affected_row>0){
                        System.out.println("RS "+ammount+" has been credited to your account successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("transiction failed!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("wrong pin!!!");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    // **********************************************************************************

    public void get_balance(long account_no){
        System.out.println("enter your security pin : ");
        String security_pin = scanner.nextLine();
        try{
            String get_balance_query = "SELECT balance FROM accounts WHERE account_no = ? AND security_key = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_balance_query);
            preparedStatement.setLong(1, account_no);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                double total_balance = resultSet.getDouble("balance");
                System.out.println("Total Balance : "+total_balance);
            }else{
                System.out.println("Invalid pin!!!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    // *****************************************************************************************

    public void transfer_money(long sender_account_no){
        System.out.println("enter recever account number : ");
        long recever_account_no = scanner.nextLong();
        scanner.nextLine();
        System.out.println("enter the ammount : ");
        double ammount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("enter your security pin : ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(sender_account_no!=0 && recever_account_no!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_no = ? AND  security_key = ?");
                preparedStatement.setLong(1, sender_account_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(ammount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance-? WHERE account_no = ?";
                        String credit_query = "UPDATE accounts SET balance = balance+? WHERE account_no = ?";
                        PreparedStatement debitstStatement = connection.prepareStatement(debit_query);
                        PreparedStatement crediStatement = connection.prepareStatement(credit_query);
                        debitstStatement.setDouble(1, ammount);
                        debitstStatement.setLong(2, sender_account_no);
                        crediStatement.setDouble(1, ammount);
                        crediStatement.setLong(2, recever_account_no);
                        int affected_row1 = debitstStatement.executeUpdate();
                        int affected_row2 = crediStatement.executeUpdate();

                        if(affected_row1>0 && affected_row2>0){
                            System.out.println("RS "+ammount+" transfered successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("trnsiction failed!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("insufficient balance!!!");
                    }
                }else{
                    System.out.println("invalid pin!!!");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
