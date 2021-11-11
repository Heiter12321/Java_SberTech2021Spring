public class OwnerImpl implements Owner {
    private final String name;
    private final String lastName;
    private final int age;
    private final int id;

    public OwnerImpl(String name, String lastName, int age, int id) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }
}
