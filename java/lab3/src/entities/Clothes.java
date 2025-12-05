package entities;

public enum Clothes {
    TIGHT_TROUSERS ("тесные клетчатые брюки", true),
    GREY_SWEATSHIRT ("серая фуфайка", false);

    private String title;
    private boolean isTight;

    Clothes(String title, boolean isTight){
        this.title = title;
        this.isTight = isTight;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean getIsTight() {
        return this.isTight;
    }

    public String toString() {
        return this.getTitle();
    }
}
