package v2;


import gui.App;
import services.*;

import javax.naming.NamingException;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JdbcReader jdbcReader = JdbcReader.getInstance();
        jdbcReader.readScheduler();
        Scheduler sScheduler = Scheduler.getInstance();
        AuditService audit = new AuditService();
        App app = new App();
        JFrame frame = new JFrame("App");
        frame.setContentPane(app.getContent());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JdbcWriter jdbcWriter = null;
                try {
                    jdbcWriter = JdbcWriter.getInstance();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                } catch (NamingException namingException) {
                    namingException.printStackTrace();
                }
                try {
                    jdbcWriter.writeScheduler(sScheduler);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        frame.pack();
        frame.setVisible(true);
        app.getAddTruckButton().addActionListener(e -> {
            int option = 1;
            MyThread myThread = new MyThread(option, true, sScheduler, app, frame, audit);
            Thread thread = new Thread(myThread);
            thread.start();
        });

        app.getAddRouteButton().addActionListener(e -> {
           int option = 3;
            MyThread myThread = new MyThread(option, true, sScheduler, app, frame, audit);
            Thread thread = new Thread(myThread);
            thread.start();
        });

        app.getMakeShippingRequestButton().addActionListener(e -> {
            int option = 10;
            MyThread myThread = new MyThread(option, true, sScheduler, app, frame, audit);
            Thread thread = new Thread(myThread);
            thread.start();
        });

        app.getScheduleShippingButton().addActionListener(e -> {
            int option = 7;
            MyThread myThread = new MyThread(option, true, sScheduler, app, frame, audit);
            Thread thread = new Thread(myThread);
            thread.start();
        });

        app.getAddUnexpectedCostButton().addActionListener(e -> {
            int option = 12;
            MyThread myThread = new MyThread(option, true, sScheduler, app, frame, audit);
            Thread thread = new Thread(myThread);
            thread.start();
        });

    }
}
