import java.util.*;

public interface Garage {
    public ArrayList<Car> cars = new ArrayList<>();
    public HashMap<Owner, ArrayList<Car>> owners = new HashMap<>();
    public HashMap<String, ArrayList<Car>> brands = new HashMap<>();
    public HashMap<Integer, Owner> ownersId = new HashMap<>();
}

