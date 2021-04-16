import java.util.Comparator;

public class CarImpl implements Car {
    private final int carId;
    private final String brand;
    private final String modelName;
    private final int maxVelocity;
    private final int power;
    private final int ownerId;

    public CarImpl(int carId, String brand, String modelName, int maxVelocity, int power, int ownerId) {
        this.brand = brand;
        this.carId = carId;
        this.modelName = modelName;
        this.maxVelocity = maxVelocity;
        this.power = power;
        this.ownerId = ownerId;
    }

    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModelName() {
        return modelName;
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public int getPower() {
        return power;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public static final Comparator<CarImpl> compareByVelocity = (o1, o2) -> o2.maxVelocity - o1.maxVelocity;
}
