import java.util.LinkedList;

public class DispatcherImpl implements Dispatcher {
    private final LinkedList<Taxi> taxisQueue;

    public DispatcherImpl() {
        this.taxisQueue = new LinkedList<>();
        System.out.println("Диспетчер создан. Мой поток: " + Thread.currentThread().toString());
    }

    public synchronized void addTaxi(Taxi taxi) {
        taxisQueue.add(taxi);
        System.out.println("Добавил такси");
    }

    @Override
    public synchronized void notifyAvailable(Taxi taxi) {
        taxisQueue.add(taxi);
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    if (!taxisQueue.isEmpty()) {
                        Taxi taxi = taxisQueue.poll();
                        synchronized (taxi) {
                            taxi.placeOrder(new Order());
                            taxi.notifyAll();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + ": Пока свободных таксистов нет, прилягу поспать");
                        this.wait();
                    }
                }
            } catch (InterruptedException ignored) {}
        }
    }
}
