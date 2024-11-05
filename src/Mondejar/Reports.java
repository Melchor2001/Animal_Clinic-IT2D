package Mondejar;

import java.util.Scanner;
import java.sql.*;

public class Reports {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Clients c = new Clients();
    Appointment a = new Appointment();
    
    public void report_type(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Report**","");
            System.out.printf("|%-5s%-95s|\n","","1. General Report");
            System.out.printf("|%-5s%-95s|\n","","2. Individual Report");
            System.out.printf("|%-5s%-95s|\n","","3. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<4){
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
                    generalReport();
                    break;
                case 2:
                    c.view();
                    IndividualView();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void generalReport(){
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Clients**","");
        c.view();
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Appointment History**","");
        a.view();
        
    }
    private void IndividualView() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Individual Report**", "");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**!Enter 0 in ID to Exit!**", "");
        System.out.print("|\tEnter ID to View: ");

        int id;
        while (true) {
            try {
                id = input.nextInt();
                if (doesIDexists(id, conf)) {
                    break;
                } else if (id == 0) {
                    exit = false;
                    break;
                } else {
                    System.out.print("|\tEnter ID to View Again: ");
                }
            } catch (Exception e) {
                input.next();
                System.out.print("|\tEnter ID to View Again: ");
            }
        }

        if (exit) {
            try {
                String clientSQL = "SELECT C_fname, C_mname, C_lname, C_gender, C_Contact, C_Payable_Amount, C_Birth_Date " +
                                   "FROM C_Clients WHERE C_Id = ?";
                PreparedStatement clientStmt = conf.connectDB().prepareStatement(clientSQL);
                clientStmt.setInt(1, id);
                ResultSet clientRs = clientStmt.executeQuery();

                if (clientRs.next()) {
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "Individual Client Information", "");
                    System.out.printf("|%-15s: %-83s|\n", "First Name", clientRs.getString("C_fname"));
                    System.out.printf("|%-15s: %-83s|\n", "Middle Name", clientRs.getString("C_mname"));
                    System.out.printf("|%-15s: %-83s|\n", "Last Name", clientRs.getString("C_lname"));
                    System.out.printf("|%-15s: %-83s|\n", "Gender", clientRs.getString("C_gender"));
                    System.out.printf("|%-15s: %-83s|\n", "Contact", clientRs.getString("C_Contact"));
                    System.out.printf("|%-15s: %-83s|\n", "Payable Amount", clientRs.getString("C_Payable_Amount"));
                    System.out.printf("|%-15s: %-83s|\n", "Birth Date", clientRs.getString("C_Birth_Date"));
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String paymentSQL = "SELECT P_Paid_Amount, P_Change, P_Balance_Left, P_Paid_Date " +
                                        "FROM Payment_History WHERE C_Id = ?";
                    PreparedStatement paymentStmt = conf.connectDB().prepareStatement(paymentSQL);
                    paymentStmt.setInt(1, id);
                    ResultSet paymentRs = paymentStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Payment History**", "");
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-22s | %-22s | %-22s | %-23s |\n", "Paid Amount", "Change", "Balance Left", "Paid Date");
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    boolean hasPayment = false;
                    while (paymentRs.next()) {
                        hasPayment = true;
                        System.out.printf("| %-22s | %-22s | %-22s | %-23s |\n",
                                paymentRs.getString("P_Paid_Amount"),
                                paymentRs.getString("P_Change"),
                                paymentRs.getString("P_Balance_Left"),
                                paymentRs.getString("P_Paid_Date"));
                    }

                    if (!hasPayment) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Payment History!!", "");
                    }

                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String appointmentSQL = "SELECT A_Type, A_Name, A_Birth_Date, A_Gender, A_Status, A_Scheduled_Date " +
                                            "FROM A_Appointments WHERE C_Id = ?";
                    PreparedStatement appointmentStmt = conf.connectDB().prepareStatement(appointmentSQL);
                    appointmentStmt.setInt(1, id);
                    ResultSet appointmentRs = appointmentStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Appointment History**", "");
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-22s | %-22s | %-22s | %-23s |\n", "Type", "Name", "Status", "Scheduled Date");
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    boolean hasAppointments = false;
                    while (appointmentRs.next()) {
                        hasAppointments = true;
                        System.out.printf("| %-22s | %-22s | %-22s | %-23s |\n",
                                appointmentRs.getString("A_Type"),
                                appointmentRs.getString("A_Name"),
                                appointmentRs.getString("A_Status"),
                                appointmentRs.getString("A_Scheduled_Date"));
                    }

                    if (!hasAppointments) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Appointment History!!", "");
                    }

                    System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");

                    String testSQL = "SELECT D_Cause_Visit, D_Treatment, D_Cost, D_Status, D_Operation_Date " +
                                     "FROM D_Test WHERE C_Id = ?";
                    PreparedStatement testStmt = conf.connectDB().prepareStatement(testSQL);
                    testStmt.setInt(1, id);
                    ResultSet testRs = testStmt.executeQuery();

                    System.out.printf("|%-75s%-50s%-75s|\n", "", "**Test/Operation History**", "");
                    System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-47s | %-97s | %-22s | %-23s |\n", "Cause of Visit", "Treatment", "Cost", "Operation Date");
                    System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");

                    boolean hasTests = false;
                    while (testRs.next()) {
                        hasTests = true;
                        System.out.printf("| %-47s | %-97s | %-22s | %-23s |\n",
                                testRs.getString("D_Cause_Visit"),
                                testRs.getString("D_Treatment"),
                                testRs.getString("D_Cost"),
                                testRs.getString("D_Operation_Date"));
                    }

                    if (!hasTests) {
                        System.out.printf("|%-75s%-50s%-75s|\n", "", "!!No Test/Operation History!!", "");
                    }

                    System.out.println("+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");

                    clientRs.close();
                    paymentRs.close();
                    appointmentRs.close();
                    testRs.close();
                    clientStmt.close();
                    paymentStmt.close();
                    appointmentStmt.close();
                    testStmt.close();

                } else {
                    System.out.println("|\tNo record found for ID: " + id + " |");
                }

            } catch (Exception e) {
                System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
            }
        }
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
}
