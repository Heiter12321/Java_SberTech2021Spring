import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GarageTest {

    @Test
    void allCarsUniqueOwners() {
        Garage garage = new GarageImpl();
        garage.addNewCar(new CarImpl(1, "BMW", "228", 200, 125, 1),
                new OwnerImpl("Jack", "Nicklson", 100, 1));

        garage.addNewCar(new CarImpl(2, "BMW", "1", 200, 125, 2),
                new OwnerImpl("Rayan", "Reynolds", 30, 2));
        assertEquals(2, garage.allCarsUniqueOwners().size());
    }

    @Test
    void topThreeCarsByMaxVelocity() {
        Garage garage = new GarageImpl();
        OwnerImpl owner = new OwnerImpl("Jack", "Nicklson", 100, 1);
        for (int i = 0; i < 3; ++i) {
            garage.addNewCar(new CarImpl(2, "BMW", "128", 300 - i*2, 125, 1),
                             owner);
        }
        ArrayList<Car> cars = (ArrayList<Car>) garage.topThreeCarsByMaxVelocity();
        for (int i = 0; i < 3; ++i) {
            assertEquals(300 - i*2, cars.get(i).getMaxVelocity());
        }
    }

    @Test
    void allCarsOfBrand() {
        Garage garage = new GarageImpl();
        Owner owner = new OwnerImpl("Jack", "Nicklson", 100, 1);
        Car car1 = new CarImpl(1, "BMW", "228", 200, 125, 1);
        Car car2 = new CarImpl(2, "BMW", "128", 300, 125, 1);
        garage.addNewCar(car1, owner);
        garage.addNewCar(car2, owner);
        ArrayList<Car> cars = (ArrayList<Car>) garage.allCarsOfBrand("BMW");
        assertEquals(car1, cars.get(0));
        assertEquals(car2, cars.get(1));
    }

    @Test
    void carsWithPowerMoreThan() {
        Garage garage = new GarageImpl();
        OwnerImpl owner = new OwnerImpl("Jack", "Nicklson", 100, 1);
        CarImpl car1 = new CarImpl(1, "BMW", "228", 200, 125, 1);
        CarImpl car2 = new CarImpl(2, "BMW", "128", 300, 225, 1);
        garage.addNewCar(car1, owner);
        garage.addNewCar(car2, owner);
        ArrayList<Car> cars = (ArrayList<Car>) garage.carsWithPowerMoreThan(100);
        assertEquals(car1, cars.get(0));
        assertEquals(car2, cars.get(1));
    }

    @Test
    void allCarsOfOwner() {
        Garage garage = new GarageImpl();
        OwnerImpl owner = new OwnerImpl("Jack", "Nicklson", 100, 1);
        CarImpl car1 = new CarImpl(1, "BMW", "228", 200, 125, 1);
        CarImpl car2 = new CarImpl(2, "BMW", "128", 300, 125, 1);
        garage.addNewCar(car1, owner);
        garage.addNewCar(car2, owner);
        ArrayList<Car> cars = (ArrayList<Car>) garage.allCarsOfOwner(owner);
        assertEquals(car1, cars.get(0));
        assertEquals(car2, cars.get(1));
    }

    @Test
    void meanOwnersAgeOfCarBrand() {
        Garage garage = new GarageImpl();
        garage.addNewCar(new CarImpl(1, "BMW", "228", 200, 125, 1),
                new OwnerImpl("Jack", "Nicklson", 100, 1));

        garage.addNewCar(new CarImpl(2, "BMW", "1", 200, 125, 2),
                new OwnerImpl("Rayan", "Reynolds", 30, 2));
        assertEquals(65, garage.meanOwnersAgeOfCarBrand("BMW"));
    }

    @Test
    void meanCarNumberForEachOwner() {
        Garage garage = new GarageImpl();
        garage.addNewCar(new CarImpl(1, "BMW", "228", 200, 125, 1),
                new OwnerImpl("Jack", "Nicklson", 100, 1));
        garage.addNewCar(new CarImpl(2, "BMW", "128", 300, 125, 1),
                new OwnerImpl("Jack", "Nicklson", 100, 1));
        assertEquals(2, garage.meanCarNumberForEachOwner());
    }
}