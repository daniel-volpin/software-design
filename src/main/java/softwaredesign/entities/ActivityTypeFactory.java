package softwaredesign.entities;

import java.util.ArrayList;

public class ActivityTypeFactory {

    public ActivityType getActivityType(String activityName)  {
        String enumName = ActivityTypeFactory.stringToEnumValue(activityName);
        ActivityTypeEnum activityType = ActivityTypeEnum.valueOf(enumName);

        if (activityType == ActivityTypeEnum.WALKING) {
            return new Walking();
        } else if (activityType == ActivityTypeEnum.RUNNING){
            return new Running();
        } else if (activityType == ActivityTypeEnum.CYCLING) {
            return new Cycling();
        } else if (activityType == ActivityTypeEnum.ROLLER_SKATING) {
            return new RollerSkating();
        } else {
            return null;
        }
    }

    public enum ActivityTypeEnum {
        WALKING,
        RUNNING,
        CYCLING,
        ROLLER_SKATING,
    }

    public static String enumValueToString(String enumValue) {
        // Convert "ROLLER_SKATING" to "Roller skating"
        char[] name = enumValue.toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return String.valueOf(name).replace("_", " ");
    }

    public static ArrayList<String> enumValuesToString() {
        ArrayList<String> activityNames = new ArrayList<>();
        for (int i = 0; i < ActivityTypeEnum.values().length; i ++) {
            ActivityTypeEnum activityType = ActivityTypeEnum.values()[i];
            activityNames.add( enumValueToString(activityType.toString()) );
        }
        return activityNames;
    }

    public static String stringToEnumValue(String activityName) {
        // i.e. Convert "Roller skating" to "ROLLER_SKATING"
        return activityName.toUpperCase().replace(" ", "_");
    }
}
