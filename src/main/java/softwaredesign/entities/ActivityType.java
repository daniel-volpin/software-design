package softwaredesign.entities;

abstract public class ActivityType {
    private String activityName;
    private Double metabolicEquivalent;

    public ActivityType(String activityType, Double metabolicEquivalent) {
        this.activityName = activityType;
        this.metabolicEquivalent = metabolicEquivalent;
    }

    public String getName() {
        return activityName;
    }
    public Double getMET()  {
        return metabolicEquivalent;
    }
}


class Walking extends ActivityType {

    public Walking(String activityName, Double metabolicEquivalent) {
        super(activityName, metabolicEquivalent);
    }
}

class Running extends ActivityType {

    public Running(String activityName, Double metabolicEquivalent) {
        super(activityName, metabolicEquivalent);
    }
}

class Cycling extends ActivityType {

    public Cycling(String activityName, Double metabolicEquivalent) {
        super(activityName, metabolicEquivalent);
    }
}

class RollerSkating extends ActivityType {

    public RollerSkating(String activityName, Double metabolicEquivalent) {
        super(activityName, metabolicEquivalent);
    }
}