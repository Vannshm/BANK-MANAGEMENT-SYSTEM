package Bank_Mng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

// ************************************************************************************

public long open_account(String email){
    if(account_exist(email)){
        System.out.println("the account is already exist");
        return -1;
    }
    String open_acc_query = "INSERT INTO accounts(account_no,name,email,balance,security_key) VALUES(?,?,?,?,?)";
    System.out.print("enter your name : ");
    String name = scanner.nextLine();

    System.out.print("enter your initial_ammount : ");
    double initial_ammount = scanner.nextDouble();
    scanner.nextLine();

    System.out.print("enter your security_pin : ");
    String security_pin = scanner.nextLine();
    try{
        long account_number = GenerateAccountNo();
        PreparedStatement preparedStatement = connection.prepareStatement(open_acc_query);
        preparedStatement.setLong(1, account_number);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, email);
        preparedStatement.setDouble(4, initial_ammount);
        preparedStatement.setString(5, security_pin);

        int affected_row = preparedStatement.executeUpdate();

        if(affected_row>0){
            return account_number;
        }else{
            System.out.println("failed to create account !!!");
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return -1;
}

// ***************************************************************************************

public long get_account_no(String email){
    String get_acc_no_query = "SELECT account_no FROM accounts WHERE email=?";
    try{
        PreparedStatement preparedStatement = connection.prepareStatement(get_acc_no_query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getLong("account_no");
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    throw new RuntimeException("account_no doesnt exist");
}

// ************************************************************************************************

private long GenerateAccountNo(){
    String generate_query = "SELECT account_no FROM accounts ORDER BY account_no DESC LIMIT 1";
    try{
        PreparedStatement preparedStatement = connection.prepareStatement(generate_query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            long last_acc_no = resultSet.getLong("account_no");
            return last_acc_no+1;
        }else{
            return 10000100;
        }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return -1;
}

// *******************************************************************************************************

public boolean account_exist(String email){
String exist_query = "SELECT account_no FROM accounts WHERE email=?";
try{
PreparedStatement preparedStatement = connection.prepareStatement(exist_query);
preparedStatement.setString(1, email);
ResultSet resultSet = preparedStatement.executeQuery();
if(resultSet.next()){
    return true;
}else{
    return false;
}
}catch(SQLException e){
    e.printStackTrace();
}
return false;
}

}
