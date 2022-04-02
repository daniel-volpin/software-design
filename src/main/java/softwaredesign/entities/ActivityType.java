package softwaredesign.entities;

abstract public class ActivityType {
    public abstract String getName();
    public abstract Double getMET();
}


class Walking extends ActivityType {
    @Override
    public String getName() { return "Walking"; }

    @Override
    public Double getMET() { return 2.9; }
}

class Running extends ActivityType {
    @Override
    public String getName() { return "Running"; }

    @Override
    public Double getMET() { return 7.0; }
}

class Cycling extends ActivityType {
    @Override
    public String getName() { return "Cycling"; }

    @Override
    public Double getMET() { return 4.0; }
}

class RollerSkating extends ActivityType {
    @Override
    public String getName() { return "Roller Skating"; }

    @Override
    public Double getMET() { return 6.5; }
}