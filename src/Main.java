import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean check = true;
        Scheduler sScheduler = Scheduler.getInstance();
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
            switch (option) {
                case 1: {
                    Scanner tempSc = new Scanner(System.in);
                    System.out.println("Truck type:");
                    System.out.println("1. Normal");
                    System.out.println("2. Refrigerated");
                    System.out.println("3. Large");
                    int type = tempSc.nextInt();
                    tempSc.nextLine();
                    Truck truck = new Truck();
                    truck.readObject();
                    sScheduler.addNewTruck(truck, type);
                    break;
                }
                case 2: {
                    sScheduler.listAvailableTrucks();
                    break;
                }
                case 3: {
                    Route route = new Route();
                    route.readObject(true);
                    sScheduler.addNewRoute(route);
                    break;
                }
                case 4: {
                    Route route = new Route();
                    route.readObject(false);
                    boolean ans = sScheduler.checkIfRouteExists(route);
                    System.out.println(ans);
                    break;
                }
                case 5: {
                    double profit = sScheduler.calculateProfit();
                    System.out.println(profit);
                    break;
                }
                case 6: {
                    sScheduler.listFinishedShippingsMonth();
                    break;
                }
                case 7: {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Insert shipping id");
                    String uuid = sc.nextLine();
                    RequestResponse res = sScheduler.scheduleShipping(uuid);
                    if (res.isResult()) {
                        System.out.println("Success");
                        System.out.println(res.getUuid());
                    } else {
                        System.out.println("Failed");
                    }
                    break;
                }
                case 9: {
                    Cargo cargo = new Cargo();
                    cargo.readObject();
                    if (!sScheduler.checkIfRouteExists(cargo.getRoute())) {
                        System.out.println("We're not available for this route");
                    } else {
                        double price = sScheduler.calculateShippingPrice(cargo);
                        System.out.println(String.format("Price for your shipping is: " + price));
                    } }
                    break;
                case 10: {
                    ShippingRequest request = new ShippingRequest();
                    request.readObject();
                    RequestResponse res = sScheduler.acceptShippingRequest(request);
                    if (res.isResult()) {
                        System.out.println("Your shipping request was accepted");
                        System.out.println("Here is your request id: " + res.getUuid());
                    } else {
                        System.out.println("Your shipping request was rejected");
                    } }
                    break;
                case 11: {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Insert shipping id:");
                    String uuid = sc.nextLine();
                    String res = sScheduler.ShippingIsFinished(uuid);
                    if (res != "") {
                        System.out.println("Success");
                        sScheduler.markTruckAsAvailable(res);
                    } else {
                        System.out.println("Couldn't find this shipping id");
                    }
                    break;
                }

                case 12: {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Insert shipping id:");
                    String uuid = sc.nextLine();
                    System.out.println("Insert new cost");
                    double cost = sc.nextDouble();
                    boolean res = sScheduler.updateShippingCost(uuid, cost);
                    if (!res) {
                        System.out.println("Couldn't find this shipping id");
                    }
                    break;
                }
                case 13: {
                    check = false;
                    break;
                }
                default: {
                    break;
                }
            }
        }

    }
}
