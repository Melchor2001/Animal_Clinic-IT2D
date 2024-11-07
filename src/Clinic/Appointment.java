package Clinic;

import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.time.LocalDate;

public class Appointment {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Clients c = new Clients();
    public void List(){
        boolean exit = true;
        do{
            System.out.println("\tManage Appointments");
            System.out.println("  1. Add");
            System.out.println("  2. Select Scheduled to Finished");
            System.out.println("  3. Select Scheduled to Canceled");
            System.out.println("  4. View");
            System.out.println("  5. Exit");
            System.out.print("Enter Choice: ");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<6){
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
                    c.view();
                    schedFinished();
                    break;
                case 3:
                    c.view();
                    schedCancel();
                    break;
                case 4:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void add(){
        boolean exit = true;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        do{
            System.out.println("\tMake Appointments");
            System.out.print("Enter Owner ID: ");
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
                        System.out.print("Enter ID to Edit Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("Enter ID to Edit Again: ");
                }
            }
            LocalDate bdate;
            LocalDate cdate = LocalDate.now();
            while(exit){
                System.out.print("Enter Animal Type: ");
                String atype = input.next();
                System.out.print("Enter Animal Name: ");
                String aname = input.next();
                String bdate2;
                while(true){
                    System.out.print("Enter Birth Date (YYYY-MM-DD): ");
                    try{
                        bdate2 = input.next();
                        bdate = LocalDate.parse(bdate2,dateFormat);
                        if(bdate.isBefore(cdate)&&bdate.isAfter(cdate.minusYears(100))){
                            break;
                        }else{
                            System.out.println("Animal Above 100 should be taken special care");
                        }
                    }catch(Exception e){
                        System.out.println("Instruction (YYYY-MM-DD)!");
                    }
                }
                String gender;
                while(true){
                    System.out.print("Gender: ");
                    try{
                        gender = input.next();
                        if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                            break;
                        }
                    }catch(Exception e){

                    }
                }
                String stat = "Scheduled";
                String SQL = "INSERT INTO A_Appointments (C_Id, A_Type, A_Name, A_Birth_Date, A_Gender, A_Status, A_Scheduled_Date) Values (?,?,?,?,?,?,?)";
                conf.addRecord(SQL, id, atype, aname, bdate, gender, stat, cdate);
                exit=false;
            }
        }while(exit);
    }
    private void schedFinished(){
        boolean exit = true;
        System.out.println("\tSet Scheduled To Finished");
        System.out.print("Enter ID of Owner: ");
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
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        while(exit){
            displaySched(id);
            System.out.print("Enter ID to set Finished: ");
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
                        System.out.print("Enter ID Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("Enter ID Again: ");
                }
            }
            while(exit){
                String stat = "Finished";
                String SQL = "UPDATE A_Appointments SET A_Status = ? Where A_Id = ?";
                conf.updateRecord(SQL, stat, Aid);
                exit=false;
            }
            exit = false;
        }
    }
    private void schedCancel(){
        boolean exit = true;
        System.out.println("\nSet Scheduled To Cancel");
        System.out.print("Enter ID of Owner: ");
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
                    System.out.print("Enter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("Enter ID Again: ");
            }
        }
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        while(exit){
            displaySched(id);
            System.out.print("Enter ID to set Cancel: ");
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
                        System.out.print("Enter ID Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("Enter ID Again: ");
                }
            }
            while(exit){
                String stat = "Cancelled";
                String SQL = "UPDATE A_Appointments SET A_Status = ? Where A_Id = ?";
                conf.updateRecord(SQL, stat, Aid);
                exit=false;
            }
            exit = false;
        }
    }
    public void view(){
        String tbl_view = "SELECT * FROM A_Appointments";
        String[] tbl_Headers = {"ID", "Owner ID","Animal Type", "Animal Name", "Status"};
        String[] tbl_Columns = {"A_Id", "C_Id","A_Type", "A_Name", "A_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    public void displaySched(int id){
        String tbl_view = "SELECT * FROM A_Appointments Where A_Status = 'Scheduled' AND C_Id = "+id;
        String[] tbl_Headers = {"ID", "Animal Type", "Animal Name", "Status"};
        String[] tbl_Columns = {"A_Id", "A_Type", "A_Name", "A_Status"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
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
}
