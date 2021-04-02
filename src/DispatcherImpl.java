import java.util.HashSet;

public class DispatcherImpl implements Dispatcher {
    private final HashSet<Taxi> taxisSet;

    public DispatcherImpl() {
        this.taxisSet = new HashSet<>();
        System.out.println("Диспетчер создан. Мой поток: " + Thread.currentThread().toString());
    }

    public synchronized void addTaxi(Taxi taxi) {
        taxisSet.add(taxi);
        System.out.println("Добавил такси");
    }

    @Override
    public synchronized void notifyAvailable(Taxi taxi) {
        taxisSet.add(taxi);
    }

    @Override
    public void run() {
        while (true) {
            if (!taxisSet.isEmpty()) {
                int index = (int) (Math.random() * taxisSet.size());
                Taxi taxi = (Taxi) taxisSet.toArray()[index];

                synchronized (taxisSet.toArray()[index]) {
                    taxi.placeOrder(new Order());
                    taxisSet.remove(taxi);
                    taxi.notify();
                }
            } else {
                try {
                    synchronized (this) {
                        System.out.println(Thread.currentThread().getName() + ": Пока свободных таксистов нет, прилягу поспать");
                        this.wait();
                    }
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
