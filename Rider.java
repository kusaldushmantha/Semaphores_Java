import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Rider implements Runnable{

    private BusStop busstop;
    private final ScheduledExecutorService scheduler;

    private final int TERMINATION_TIME = 60*60*2;
    private final int TURN_DELAY = 20;
    private final int INITIAL_DELAY = 0;

    public Rider(BusStop busStop){
        this.busstop = busStop;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void riderScheduler(){
        final Runnable rider = new Runnable() {
            @Override
            public void run() {
                runMethod();
            }
        };

        final ScheduledFuture handleRider = scheduler.scheduleWithFixedDelay(this::run, INITIAL_DELAY
                , TURN_DELAY, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                handleRider.cancel(true);
                System.exit(0);
            }
        },TERMINATION_TIME,TimeUnit.SECONDS);
    }

    public void runMethod() {
        try {
            busstop.getRiders_mutex().acquire();
            System.out.println("Riders mutex acquired by riderID: "+ Thread.currentThread().getId());
            busstop.getRiders().incrementAndGet();
            busstop.getRiders_mutex().release();
            System.out.println("Riders mutex released by riderID: "+ Thread.currentThread().getId());

            busstop.getBus_arrived_signal().acquire();
            System.out.println("Bus signal acquired by riderID: "+Thread.currentThread().getId());

            boardBus();

            busstop.getRider_aboard_signal().release();
            System.out.println("RiderID: "+Thread.currentThread().getId()+" boarded and released semaphore");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void boardBus() {
        System.out.println("RiderID: "+Thread.currentThread().getId()+" boarding bus");
    }

    @Override
    public void run() {
        this.runMethod();
    }
}
