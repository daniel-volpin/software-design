package softwaredesign.entities;

public class Profile {
    private static Profile profileInstance;
    private boolean isInit = false;
    private double height;
    private double weight;
    private int age;

    public static Profile getInstance() {
        if (profileInstance == null){
            profileInstance = new Profile();
        }
        return profileInstance;
    }

    public boolean isInitialized() {return isInit;}
    public void initialize() {isInit = true;}
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
    public void setHeight(double height) { this.height = height; }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
