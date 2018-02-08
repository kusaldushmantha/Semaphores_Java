public class Main {

    public static void main(String[] args) {
        int no_riders = 120;

        BusStop busStop = new BusStop();

        new Bus(busStop).bus_scheduler();

        for(int i=0;i<no_riders;i++){
            new Rider(busStop).riderScheduler();
        }
    }
}
