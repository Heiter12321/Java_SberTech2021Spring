public class Main {
    public static void main(String[] args) {
        DispatcherImpl dispatcher = new DispatcherImpl();

        Thread thread = new Thread(dispatcher);

        Taxi taxi1 = new TaxiImpl(dispatcher);
        Taxi taxi2 = new TaxiImpl(dispatcher);
        Taxi taxi3 = new TaxiImpl(dispatcher);
        Taxi taxi4 = new TaxiImpl(dispatcher);


        Thread thread1 = new Thread(taxi1);
        Thread thread2 = new Thread(taxi2);
        Thread thread3 = new Thread(taxi3);
        Thread thread4 = new Thread(taxi4);

        dispatcher.addTaxi(taxi1);
        dispatcher.addTaxi(taxi2);
        dispatcher.addTaxi(taxi3);
        dispatcher.addTaxi(taxi4);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread.start();
    }
}
