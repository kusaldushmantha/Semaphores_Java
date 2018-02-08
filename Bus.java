import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Bus implements Runnable{

    public Bus(BusStop busStop){
        this.busStop = busStop;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    private final BusStop busStop;
    private final ScheduledExecutorService scheduler;

    private final int bus_capacity = 50;
    private final int TERMINATION_TIME = 60*60*2;
    private final int TURN_DELAY = 60*2;
    private final int INITIAL_DELAY = 0;

    void bus_scheduler() {
        final Runnable bus = new Runnable(){
            @Override
            public void run() {
                runMethod();
            }
        };

        final ScheduledFuture handleBus = scheduler.scheduleWithFixedDelay(this::run, INITIAL_DELAY
                , TURN_DELAY, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handleBus.cancel(true);
                System.exit(0);
            }
        },TERMINATION_TIME,TimeUnit.SECONDS);
    }

    public void runMethod() {
        try {
            busStop.getRiders_mutex().acquire();
            System.out.println("Riders mutex acquired by Bus");

            int no_boarding_riders = Math.min(busStop.getRiders().get(),bus_capacity);
            System.out.println("Waiting riders at bus stop: "+ busStop.getRiders().get());
            System.out.println("Boarding riders count: "+no_boarding_riders);

            for(int i=0;i<no_boarding_riders;i++){
                busStop.getBus_arrived_signal().release();
                System.out.println("Bus semaphore released by bus, Rider can now get in");

                busStop.getRider_aboard_signal().acquire();
                System.out.println("Rider boarding the bus acquired the Semaphore");
            }

            int n = busStop.getRiders().get();
            busStop.getRiders().set(Math.max((n - 50), 0));
            busStop.getRiders_mutex().release();

            System.out.println("Riders mutex released by Bus");
            departBus();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void departBus() {
        System.out.println("===================Bus with BusID : " + Thread.currentThread().getId() + " departs===================");
    }

    @Override
    public void run() {
        this.runMethod();
    }
}
