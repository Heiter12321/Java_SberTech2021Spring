public class TaxiImpl implements Taxi {
    public volatile Order order;
    public final Dispatcher dispatcher;

    TaxiImpl(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                sleepUntilThereIsNoOrder();

                doOrder();

                System.out.println(Thread.currentThread().getName() + ": Бужу диспетчера");
            }
            synchronized (dispatcher) {
                dispatcher.notifyAvailable(this);
                dispatcher.notifyAll();
            }
        }
    }

    public void sleepUntilThereIsNoOrder() {
        try {
            while (order == null) {
                System.out.println(Thread.currentThread().getName() + ": Заказов нет, прилягу поспать");
                this.wait();
            }
        } catch (InterruptedException ignored) {}
    }

    public void doOrder() {
        System.out.println(Thread.currentThread().getName() + ": Начинаю выполнять заказ");

        try {
            Thread.sleep(order.time);
        } catch (InterruptedException ignored) {}

        order = null;
        System.out.println(Thread.currentThread().getName() + ": Заказ выполнен");
    }

    @Override
    public void placeOrder(Order order) {
        this.order = order;
    }
}
