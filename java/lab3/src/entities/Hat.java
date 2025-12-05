package entities;

public record Hat(String title, boolean isWideVisor) {
    public String title() {
        if (this.isWideVisor) {
            return this.title + " с широким козырьком";
        } else {
            return this.title;
        }
    }

    public String toString() {
        return this.title();
    }
}
