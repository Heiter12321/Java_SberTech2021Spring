public class TaxiImpl implements Taxi {
    public volatile Order order;
    public final Dispatcher dispatcher;

    TaxiImpl(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (true) {
            if (order == null) {
                synchronized (this) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ": Заказов нет, прилягу поспать");
                        this.wait();
                    } catch (InterruptedException ignored) {}
                }
            }

            System.out.println(Thread.currentThread().getName() + ": Начинаю выполнять заказ");

            try {
                Thread.sleep(order.time);
            } catch (InterruptedException ignored) {}

            order = null;
            System.out.println(Thread.currentThread().getName() + ": Заказ выполнен");
            System.out.println(Thread.currentThread().getName() + ": Бужу диспетчера");

            synchronized (dispatcher) {
                dispatcher.notifyAvailable(this);
                dispatcher.notify();
            }
        }

    }

    @Override
    public void placeOrder(Order order) {
        this.order = order;
    }
}
