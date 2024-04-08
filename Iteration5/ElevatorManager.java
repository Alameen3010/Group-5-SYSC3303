public class ElevatorManager {

    private static void log(String message) {
        System.out.println("[FloorSubsystem] " + message);
    }


    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort, 1);
        new Thread(elevatorSubsystem).start();
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            elevatorSubsystem.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
            log(elevatorSubsystem.measurements());
        }));

        Elevator elevatorSubsystem2 = new Elevator(schedulerIP, schedulerPort, 2);
        new Thread(elevatorSubsystem2).start();
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            elevatorSubsystem2.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
            log(elevatorSubsystem2.measurements());
        }));

        Elevator elevatorSubsystem3 = new Elevator(schedulerIP, schedulerPort, 3);
        new Thread(elevatorSubsystem3).start();
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            elevatorSubsystem3.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
            log(elevatorSubsystem3.measurements());
        }));

        Elevator elevatorSubsystem4 = new Elevator(schedulerIP, schedulerPort, 4);
        new Thread(elevatorSubsystem4).start();
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            elevatorSubsystem4.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
            log(elevatorSubsystem4.measurements());
        }));
    }
}
