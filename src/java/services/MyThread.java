package services;

import entity.*;
import gui.App;

import javax.swing.*;
import java.util.Scanner;

public class MyThread implements Runnable{
    private final int option;
    private final boolean ui;
    Scheduler scheduler;
    App app;
    JFrame frame;
    AuditService audit;

    public MyThread(int option, boolean ui, Scheduler scheduler, App app, JFrame frame, AuditService audit) {
        this.option = option;
        this.ui = ui;
        this.scheduler = scheduler;
        this.app = app;
        this.frame = frame;
        this.audit = audit;
    }

    @Override
    public void run() {
        switch (option) {
            case 1: {
                int type = 1;
                Truck truck = new Truck();
                if (!this.ui) {
                    Scanner tempSc = new Scanner(System.in);
                    System.out.println("Truck type:");
                    System.out.println("1. Normal");
                    System.out.println("2. Refrigerated");
                    System.out.println("3. Large");
                    type = tempSc.nextInt();
                    tempSc.nextLine();
                    truck.readObject();
                } else {
                    String truckType = this.app.getTruckTypesComboBox().getItemAt(this.app.getTruckTypesComboBox().getSelectedIndex());
                    switch (truckType) {
                        case "1.Normal truck":
                            type = 1;
                            break;
                        case "2.Refrigerated truck":
                            type = 2;
                            break;
                        case "3.Large truck":
                            type = 3;
                            break;
                        default:
                            break;
                    }
                    truck.readObject(app);
                }
                audit.saveFunctionCall("addNewTruck()", Thread.currentThread().getName());
                this.scheduler.addNewTruck(truck, type);
                break;
            }
            case 2: {
                this.scheduler.listAvailableTrucks();
                audit.saveFunctionCall("listAvailableTrucks()", Thread.currentThread().getName());
                break;
            }
            case 3: {
                Route route = new Route();
                if (!this.ui) {
                    route.readObject(true);
                } else {
                    route.readObject(app, true);
                }
                this.scheduler.addNewRoute(route);
                audit.saveFunctionCall("addNewRoute()", Thread.currentThread().getName());
                break;
            }
            case 4: {
                Route route = new Route();
                route.readObject(false);
                boolean ans = this.scheduler.checkIfRouteExists(route);
                System.out.println(ans);
                audit.saveFunctionCall("checkIfRouteExists", Thread.currentThread().getName());
                break;
            }
            case 5: {
                double profit = this.scheduler.calculateProfit();
                System.out.println(profit);
                audit.saveFunctionCall("calculateProfit()", Thread.currentThread().getName());
                break;
            }
            case 6: {
                this.scheduler.listFinishedShippingsMonth();
                audit.saveFunctionCall("listFinishedShippingsMonth()", Thread.currentThread().getName());
                break;
            }
            case 7: {
                if (!this.ui) {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Insert shipping id");
                    String uuid = sc.nextLine();
                    RequestResponse res = this.scheduler.scheduleShipping(uuid);
                    if (res.isResult()) {
                        System.out.println("Success");
                        System.out.println(res.getUuid());
                    } else {
                        System.out.println("Failed");
                    }
                } else {
                    String uuid = app.getScheduleShippingID().getText();
                    RequestResponse res = this.scheduler.scheduleShipping(uuid);
                    if (res.isResult()) {
                        String log = "Your shipping id: " + res.getUuid();
                        JOptionPane.showMessageDialog(this.frame, log, "Shipping scheduled", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this.frame, "Shipping not found", "Request rejected", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                audit.saveFunctionCall("scheduleShipping()", Thread.currentThread().getName());
                break;
            }
            case 9: {
                Cargo cargo = new Cargo();
                cargo.readObject();
                if (!this.scheduler.checkIfRouteExists(cargo.getRoute())) {
                    System.out.println("We're not available for this route");
                } else {
                    double price = this.scheduler.calculateShippingPrice(cargo);
                    System.out.println("Price for your shipping is: " + price);
                }
                audit.saveFunctionCall("calculateShippingPrice()", Thread.currentThread().getName());
                break;
            }
            case 10: {
                ShippingRequest request = new ShippingRequest();
                if (!this.ui) {
                    request.readObject();
                } else {
                    request.readObject(this.app);
                }
                RequestResponse res = this.scheduler.acceptShippingRequest(request);
                if (!this.ui){
                    if (res.isResult()) {
                        System.out.println("Your shipping request was accepted");
                        System.out.println("Here is your request id: " + res.getUuid());
                    } else {
                        System.out.println("Your shipping request was rejected");
                    }
                } else {
                    if (res.isResult()) {
                        String log = "Your shipping id: " + res.getUuid();
                        JOptionPane.showMessageDialog(this.frame, log, "Shipping scheduled", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this.frame, "Shipping not found", "Request rejected", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                audit.saveFunctionCall("acceptShippingRequest()", Thread.currentThread().getName());
                break;
            }
            case 11: {
                Scanner sc = new Scanner(System.in);
                System.out.println("Insert shipping id:");
                String uuid = sc.nextLine();
                String res = this.scheduler.ShippingIsFinished(uuid);
                if (!res.equals("")) {
                    System.out.println("Success");
                    this.scheduler.markTruckAsAvailable(res);
                } else {
                    System.out.println("Couldn't find this shipping id");
                }
                audit.saveFunctionCall("ShippingIsFinished", Thread.currentThread().getName());
                break;
            }

            case 12: {
                if (!this.ui) {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Insert shipping id:");
                    String uuid = sc.nextLine();
                    System.out.println("Insert new cost");
                    double cost = sc.nextDouble();
                    boolean res = this.scheduler.updateShippingCost(uuid, cost);
                    if (!res) {
                        System.out.println("Couldn't find this shipping id");
                    }
                } else {
                    String uuid = app.getUnexpectedCostTextField1().getText();
                    double cost = Double.parseDouble(app.getUnexpectedCostTextField2().getText());
                    boolean res = this.scheduler.updateShippingCost(uuid, cost);
                    if (!res) {
                        JOptionPane.showMessageDialog(this.frame, "Shipping not found", "Request rejected", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this.frame, "cost added", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                audit.saveFunctionCall("updateShippingCost", Thread.currentThread().getName());
                break;
            }
            default: {
                break;
            }
        }
    }
}
