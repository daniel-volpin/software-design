package softwaredesign.entities;

public class Profile {
    private double height;
    private double weight;
    private int age;

    public Profile(double height, double weight, int age){
        this.height = height;
        this.weight = weight;
        this.age = age;
    }
    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

}
