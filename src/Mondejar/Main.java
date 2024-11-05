package Mondejar;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Animal Clinic System**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Clients");
            System.out.printf("|%-5s%-95s|\n", "", "2. Manage Animals");
            System.out.printf("|%-5s%-95s|\n", "", "3. Test and Payment");
            System.out.printf("|%-5s%-95s|\n", "", "4. Reports");
            System.out.printf("|%-5s%-95s|\n", "", "5. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
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
            switch (choice) {
                case 1:
                    Clients c = new Clients();
                    c.list();
                    break;
                case 2:
                    Appointment a = new Appointment();
                    a.List();;
                    break;
                case 3:
                    Test t = new Test();
                    t.test();
                    break;
                case 4:
                    Reports r = new Reports();
                    r.report_type();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }
}
