package entities;

public class Pistol {
    protected String title;
    protected boolean isCharged = false;
    protected boolean isEquipped = false;

    public Pistol(String title) {
        this.title = title;
    }

    public void equip() {
        this.isEquipped = true;
    }

    public void charge() {
        this.isCharged = true;
    }

    public void discharge() {
        this.isCharged = false;
    }

    public boolean getIsEquipped() {
        return this.isEquipped;
    }

    public boolean getIsCharged() {
        return this.isCharged;
    }

    public String getTitle() {
        return this.title;
    }

    public String toString() {
        return this.getTitle();
    }
}
