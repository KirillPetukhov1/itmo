package entities;

public enum Feeling {
    HAPPY ("веселый", true),
    SAD ("грустный", false),
    ANGRY ("злой", true),
    SCARED ("испуганный", false);

    private String title;
    private boolean isActive;

    Feeling(String title, boolean isActive){
        this.title = title;
        this.isActive = isActive;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public String toString() {
        return this.getTitle();
    }
}
