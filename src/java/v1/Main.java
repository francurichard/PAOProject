package v1;

import java.sql.SQLException;
import java.util.Scanner;
import services.*;

import javax.naming.NamingException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, NamingException {
        JdbcReader jdbcReader = JdbcReader.getInstance();
        jdbcReader.readScheduler();
        Scheduler sScheduler = Scheduler.getInstance();
        AuditService audit = new AuditService();
        boolean check = true;
        while (check) {
            System.out.println("You have the following options:");
            System.out.println("Company options:");
            System.out.println("1. Add a new truck");
            System.out.println("2. List available trucks");
            System.out.println("3. Add a new route");
            System.out.println("4. Check if route exists");
            System.out.println("5. Show company's profit");
            System.out.println("6. List finished shipping");
            System.out.println("7. Schedule shipping");
            System.out.println("Client options:");
            System.out.println("9. Calculate price for a shipping");
            System.out.println("10. Make a shipping request");
            System.out.println("Truck driver options:");
            System.out.println("11. Alert schedule that shipping is finished");
            System.out.println("12. Alert schedule for unexpected shipping cost");
            System.out.println("13. Exit program");

            Scanner mainSc = new Scanner(System.in);
            int option = mainSc.nextInt();
            if (option == 13) {
                check = false;
            } else {
                MyThread myThread = new MyThread(option, false, sScheduler, null, null, audit);
                Thread thread = new Thread(myThread);
                thread.start();
            }
        }
        JdbcWriter jdbcWriter = JdbcWriter.getInstance();
        jdbcWriter.writeScheduler(sScheduler);
    }
}
