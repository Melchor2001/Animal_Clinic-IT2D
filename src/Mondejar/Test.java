package Mondejar;

import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.time.LocalDate;

public class Test {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Clients c = new Clients();
    
    public void test(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Test**","");
            System.out.printf("|%-5s%-95s|\n","","1. Test");
            System.out.printf("|%-5s%-95s|\n","","2. Pay Balance");
            System.out.printf("|%-5s%-95s|\n","","3. View");
            System.out.printf("|%-5s%-95s|\n","","4. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<5){
                        break;
                    }else{
                        System.out.printf("|%-5sEnter Choice Again: ","");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ","");
                }
            }
            switch(choice){
                case 1:
                    c.view();
                    add();
                    break;
                case 2:
                    viewBalance();
                    pay();
                    break;
                case 3:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void pay(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Paying Balance**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID of Client: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            double cbalance = getCurrentBalance(id);
            double cash;
            System.out.println("|\tPayable Amount: "+cbalance);
            System.out.print("|\tEnter Amount to Pay: ");
            while(true){
                try{
                    cash = input.nextDouble();
                    if(cash>=0){
                        break;
                    }else{
                        System.out.print("|\tEnter Amount to Pay Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter Amount to Pay Again: ");
                }
            }
            double Gcash;
            if(cbalance >= cash){
                cbalance -= cash;
                String SQL = "INSERT INTO Payment_History (C_Id, P_Paid_Amount, P_Change, P_Balance_Left, P_Paid_Date) Values (?,?,?,?,?)";
                conf.addRecord(SQL, id, cash, 0.0, cbalance, cdate);
                String SQL2 = "UPDATE C_Clients SET C_Payable_Amount = ? Where C_Id = ?";
                conf.updateRecord(SQL2, cbalance, id);
            }else{
                Gcash = cash-cbalance;
                String SQL = "INSERT INTO Payment_History (C_Id, P_Paid_Amount, P_Change, P_Balance_Left, P_Paid_Date) Values (?,?,?,?,?)";
                conf.addRecord(SQL, id, cash, Gcash, 0.0, cdate);
                String SQL2 = "UPDATE C_Clients SET C_Payable_Amount = ? Where C_Id = ?";
                conf.updateRecord(SQL2, 0.0, id);
            }
            exit = false;
        }
    }
    private void add(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Animal Test Result**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID of Owner: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            displaySched(id);
            System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
            System.out.print("|\tEnter ID of Animal to Test: ");
            int Aid;
            while(true){
                try{
                    Aid = input.nextInt();
                    if(doesIDexists2(Aid, conf, id)){
                        break;
                    }else if(Aid == 0){
                        exit = false;
                        break;
                    }else{
                        System.out.print("|\tEnter ID Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter ID Again: ");
                }
            }
            while(exit){
                System.out.print("|\tEnter Cause of Visit (ex. Injury, Illness): ");
                input.nextLine();
                String Cvisit;
                while (true) {
                    try {
                        Cvisit = input.nextLine();
                        break;
                    } catch (Exception e) {
                        System.out.print("|\tEnter Cause Again: ");
                    }
                }

                System.out.print("|\tEnter Treatment (ex. Pain Relievers, Tumor Removal): ");
                String treatment;
                while (true) {
                    try {
                        treatment = input.nextLine();
                        break;
                    } catch (Exception e) {
                        System.out.print("|\tEnter Treatment Again: ");
                    }
                }
                double cbalance = getCurrentBalance(id);
                double cash;
                System.out.print("|\tEnter Test Cost: ");
                while(true){
                    try{
                        cash = input.nextDouble();
                        if(cash>=0){
                            cbalance += cash;
                            break;
                        }else{
                            System.out.print("|\tEnter Cost Again: ");
                        }
                    }catch(Exception e){
                        input.next();
                        System.out.print("|\tEnter Cost Again: ");
                    }
                }
                System.out.print("|\tSchedule For The Next Test (Yes/No): ");
                String yn;
                while(true){
                    try{
                        yn = input.next();
                        if(yn.equalsIgnoreCase("yes")|| yn.equalsIgnoreCase("no")){
                            break;
                        }else{
                            System.out.print("|\tEnter Again (Yes/No): ");
                        }
                    }catch(Exception e){
                        System.out.print("|\tEnter Again (Yes/No): ");
                    }
                }
                String stat,stat2;
                if(yn.equalsIgnoreCase("yes")){
                    stat2 = "Re-Scheduled";
                    stat = "Scheduled";
                }else{
                    stat = "Finished";
                    stat2 = "Finished";
                }
                String SQL = "INSERT INTO D_Test (C_Id, A_Id, D_Cause_Visit, D_Treatment, D_Cost, D_Status, D_Operation_Date) Values (?,?,?,?,?,?,?)";
                conf.addRecord(SQL, id, Aid, Cvisit, treatment, cash, stat2, cdate);
                String SQL2 = "UPDATE A_Appointments SET A_Status = ? Where A_Id = ?";
                conf.updateRecord(SQL2, stat, Aid);
                String SQL3 = "UPDATE C_Clients SET C_Payable_Amount = ? Where C_Id = ?";
                conf.updateRecord(SQL3, cbalance, id);
                exit=false;
            }
            exit = false;
        }
    }
    private void displaySched(int id){
        String tbl_view = "SELECT * FROM A_Appointments Where A_Status = 'Scheduled' AND C_Id = "+id;
        String[] tbl_Headers = {"ID", "Animal Type", "Animal Name", "Status"};
        String[] tbl_Columns = {"A_Id", "A_Type", "A_Name", "A_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    public void view(){
        String tbl_view = "SELECT * FROM D_Test";
        String[] tbl_Headers = {"ID", "Owner ID", "Animal ID", "Date Operation"};
        String[] tbl_Columns = {"D_Id", "C_Id", "A_Id", "D_Operation_Date"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    public void viewBalance(){
        String tbl_view = "SELECT * From C_Clients";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Balance"};
        String[] tbl_Columns = {"C_Id", "C_fname", "C_lname", "C_Payable_Amount"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    
    //validation sa ubos
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM C_Clients Where C_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private boolean doesIDexists2(int id, config conf, int ownerID) {
        String query = "SELECT COUNT(*) FROM A_Appointments Where A_Id = ? AND A_Status = 'Scheduled' AND C_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, ownerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
    private double getCurrentBalance(int id) {
        String query = "SELECT C_Payable_Amount FROM C_Clients WHERE C_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("C_Payable_Amount");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving balance: " + e.getMessage());
        }
        return 0.0;
    }
}
