import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class BusStop {

    private AtomicInteger riders = new AtomicInteger(0);
    private Semaphore riders_mutex = new Semaphore(1);
    private Semaphore bus_arrived_signal = new Semaphore(0);
    private Semaphore rider_aboard_signal = new Semaphore(0);


    public Semaphore getRiders_mutex() {
        return riders_mutex;
    }

    public void setRiders_mutex(Semaphore riders_mutex) {
        this.riders_mutex = riders_mutex;
    }

    public Semaphore getBus_arrived_signal() {
        return bus_arrived_signal;
    }

    public void setBus_arrived_signal(Semaphore bus_arrived_signal) {
        this.bus_arrived_signal = bus_arrived_signal;
    }

    public Semaphore getRider_aboard_signal() {
        return rider_aboard_signal;
    }

    public void setRider_aboard_signal(Semaphore rider_aboard_signal) {
        this.rider_aboard_signal = rider_aboard_signal;
    }

    public AtomicInteger getRiders() {
        return riders;
    }

    public void setRiders(AtomicInteger riders) {
        this.riders = riders;
    }
}
