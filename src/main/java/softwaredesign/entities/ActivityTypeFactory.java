package softwaredesign.entities;

public class ActivityTypeFactory {

    public ActivityType getActivityType(String activityName)  {

        if (activityName == "Walking") {
            return new Walking("Walking", 2.9);
        } else if (activityName == "Running"){
            return new Running("Running", 7.0);
        } else if (activityName == "Cycling") {
            return new Cycling("Cycling", 4.0);
        } else if (activityName == "Roller Skating") {
            return new RollerSkating("Cycling", 6.5);
        } else {
            return null;
        }
    }
}
