import java.util.*;

public class GarageImpl implements Garage {
    private final ArrayList<Car> cars = new ArrayList<>();
    private final HashMap<Owner, ArrayList<Car>> owners = new HashMap<>();
    private final PriorityQueue<CarImpl> priorityCars = new PriorityQueue<>(CarImpl.compareByVelocity);
    private final HashMap<String, ArrayList<Car>> brands = new HashMap<>();
    private final HashMap<Integer, Owner> ownersId = new HashMap<>();

    public Collection<Owner> allCarsUniqueOwners() {
        return new ArrayList<>(owners.keySet());
    }

    /**
     * Complexity should be less than O(n)
     */
    public Collection<Car> topThreeCarsByMaxVelocity() {
        ArrayList<Car> topThreeCars = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            topThreeCars.add(priorityCars.poll());
        }
        for (int i = 0; i < 3; ++i) {
            priorityCars.add((CarImpl) topThreeCars.get(i));
        }
        return topThreeCars;
    }

    /**
     * Complexity should be O(1)
     */
    public Collection<Car> allCarsOfBrand(String brand) {
        return brands.get(brand);
    }

    /**
     * Complexity should be less than O(n)
     */
    public Collection<Car> carsWithPowerMoreThan(int power) {
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
    public Collection<Car> allCarsOfOwner(Owner owner) {
        return owners.get(owner);
    }

    /**
     * @return mean value of owner age that has cars with given brand
     */
    public int meanOwnersAgeOfCarBrand(String brand) {
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
    public int meanCarNumberForEachOwner() {
        return cars.size() / ownersId.size();
    }

    /**
     * Complexity should be less than O(n)
     * @return removed car
     */
    public Car removeCar(int carId) {
        for (Car car : cars) {
            if (car.getCarId() == carId) {
                cars.remove(car);

                priorityCars.remove(car);

                ArrayList<Car> newArrayCars = brands.get(car.getBrand());
                newArrayCars.removeIf(c -> c.getCarId() == carId);
                brands.put(car.getBrand(), newArrayCars);

                ArrayList<Car> newArrayCars2;

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
    public void addNewCar(Car car, Owner owner) {
        cars.add(car);
        priorityCars.add((CarImpl) car);

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

