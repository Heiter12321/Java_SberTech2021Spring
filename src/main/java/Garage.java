import java.util.*;

public interface Garage {
    public ArrayList<Car> cars = new ArrayList<>();
    public HashMap<Owner, ArrayList<Car>> owners = new HashMap<>();
    public PriorityQueue<Car> priorityCars = new PriorityQueue<>(Car.compareByVelocity);
    public HashMap<String, ArrayList<Car>> brands = new HashMap<>();
    public HashMap<Integer, Owner> ownersId = new HashMap<>();

    public default Collection<Owner> allCarsUniqueOwners() {
        return new ArrayList<Owner>(owners.keySet());
    }

    /**
     * Complexity should be less than O(n)
     */
    public default Collection<Car> topThreeCarsByMaxVelocity() {
        ArrayList<Car> topThreeCars = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            topThreeCars.add(priorityCars.poll());
        }
        for (int i = 0; i < 3; ++i) {
            priorityCars.add(topThreeCars.get(i));
        }
        return topThreeCars;
    }

    /**
     * Complexity should be O(1)
     */
    public default Collection<Car> allCarsOfBrand(String brand) {
        return brands.get(brand);
    }

    /**
     * Complexity should be less than O(n)
     */
    public default Collection<Car> carsWithPowerMoreThan(int power) {
        ArrayList<Car> carsWithPowerMore = new ArrayList<>();
        for (Car car : cars) {
            if (car.getPower() > power) {
                carsWithPowerMore.add(car);
            }
        }
        return carsWithPowerMore;
    }

    /**
     * Complexity should be O(1)
     */
    public default Collection<Car> allCarsOfOwner(Owner owner) {
        return owners.get(owner);
    }

    /**
     * @return mean value of owner age that has cars with given brand
     */
    public default int meanOwnersAgeOfCarBrand(String brand) {
        ArrayList<Car> carsWithGivenBrand = brands.get(brand);
        HashSet<Owner> ownersWithNeededCar = new HashSet<>();
        for (Car car : carsWithGivenBrand) {
            ownersWithNeededCar.add(ownersId.get(car.getOwnerId()));
        }
        int sum = 0;
        for (Owner owner : ownersWithNeededCar) {
            sum += owner.getAge();
        }
        return sum / ownersWithNeededCar.size();
    }

    /**
     * @return mean value of cars for all owners
     */
    public default int meanCarNumberForEachOwner() {
        return cars.size() / ownersId.size();
    }

    /**
     * Complexity should be less than O(n)
     * @return removed car
     */
    public default Car removeCar(int carId) {
        for (Car car : cars) {
            if (car.getCarId() == carId) {
                cars.remove(car);

                priorityCars.remove(car);

                ArrayList<Car> newArrayCars = brands.get(car.getBrand());
                newArrayCars.removeIf(c -> c.getCarId() == carId);
                brands.put(car.getBrand(), newArrayCars);

                ArrayList<Car> newArrayCars2 = new ArrayList<>();

                newArrayCars2 = owners.get(ownersId.get(car.getOwnerId()));
                newArrayCars2.remove(car);
                owners.put(ownersId.get(car.getOwnerId()), newArrayCars2);

                return car;
            }
        }
        return null;
    }

    /**
     * Complexity should be less than O(n)
     */
    public default void addNewCar(Car car, Owner owner) {
        cars.add(car);
        priorityCars.add(car);

        ArrayList<Car> newArrayCars = new ArrayList<>();
        if (brands.containsKey(car.getBrand())) {
            newArrayCars = brands.get(car.getBrand());
        }
        newArrayCars.add(car);
        brands.put(car.getBrand(), newArrayCars);

        ArrayList<Car> newArrayCars2 = new ArrayList<>();
        if (owners.containsKey(owner)) {
            newArrayCars2 = owners.get(owner);
        }
        newArrayCars2.add(car);
        owners.put(owner, newArrayCars2);

        ownersId.put(owner.getId(), owner);
    }
}

