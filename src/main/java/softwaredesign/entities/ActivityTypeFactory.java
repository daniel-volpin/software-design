package softwaredesign.entities;

public class ActivityTypeFactory {

    public ActivityType getActivityType(String activityName)  {

        if (activityName == "Walking") {
            return new Walking();
        } else if (activityName == "Running"){
            return new Running();
        } else if (activityName == "Cycling") {
            return new Cycling();
        } else if (activityName == "Roller Skating") {
            return new RollerSkating();
        } else {
            return null;
        }
    }
}
