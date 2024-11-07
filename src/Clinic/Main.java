package Clinic;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println("\tAnimal Clinic System");
            System.out.println("  1. Clients");
            System.out.println("  2. Manage Animals");
            System.out.println("  3. Test and Payment");
            System.out.println("  4. Reports");
            System.out.println("  5. Exit");
            System.out.print("Enter Choice: ");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<6){
                        break;
                    }else{
                        System.out.print("Enter Choice Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("Enter Choice Again: ");
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    Clients c = new Clients();
                    c.list();
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    break;
                case 2:
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    Appointment a = new Appointment();
                    a.List();
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    break;
                case 3:
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    Test t = new Test();
                    t.test();
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    break;
                case 4:
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    Reports r = new Reports();
                    r.report_type();
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }
}
