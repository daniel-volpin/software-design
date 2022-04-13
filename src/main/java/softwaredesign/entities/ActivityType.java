package softwaredesign.entities;

import softwaredesign.entities.ActivityTypeFactory.ActivityTypeEnum;

abstract public class ActivityType {
    public abstract Double getMET();
    public abstract ActivityTypeEnum getType();

    public String getName() {
        return ActivityTypeFactory.enumValueToString(getType().toString());
    }
}


class Walking extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.WALKING; }

    @Override
    public Double getMET() { return 2.9; }
}

class Running extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.RUNNING; }

    @Override
    public Double getMET() { return 7.0; }
}

class Cycling extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.CYCLING; }

    @Override
    public Double getMET() { return 4.0; }
}

class RollerSkating extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.ROLLER_SKATING; }

    @Override
    public Double getMET() { return 6.5; }
}

class Swimming extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.SWIMMING; }

    @Override
    public Double getMET() { return 5.8; }
}

class HorseRiding extends ActivityType {
    @Override
    public ActivityTypeEnum getType() { return ActivityTypeEnum.HORSE_RIDING; }

    @Override
    public Double getMET() { return 5.5; }
}