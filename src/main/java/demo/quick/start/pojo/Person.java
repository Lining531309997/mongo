package demo.quick.start.pojo;

import org.bson.types.ObjectId;

/**
 * Created by lining on 2019/10/10.
 */
public final class Person {
    private ObjectId id;
    private String name;
    private int age;
    private Address address;

    /**
     * Construct a new instance
     */
    public Person() {
    }

    /**
     * Construct a new instance
     *
     * @param name the name
     * @param age the age
     * @param address the address
     */
    public Person(final String name, final int age, final Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        if (getAge() != person.getAge()) {
            return false;
        }
        if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null) {
            return false;
        }
        if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null) {
            return false;
        }
        if (getAddress() != null ? !getAddress().equals(person.getAddress()) : person.getAddress() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getAge();
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{"
                + "id='" + id + "'"
                + ", name='" + name + "'"
                + ", age=" + age
                + ", address=" + address
                + "}";
    }
}
