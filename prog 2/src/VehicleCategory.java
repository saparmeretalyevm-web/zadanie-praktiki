public enum VehicleCategory {
    SEDAN("Седан", "#4CAF50"),
    SUV("Внедорожник", "#FF9800"),
    ELECTRIC("Электро", "#2196F3"),
    HATCHBACK("Хэтчбек", "#9C27B0"),
    COUPE("Купе", "#E91E63"),
    TRUCK("Грузовик", "#795548");

    private final String displayName;
    private final String colorCode;

    VehicleCategory(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    @Override
    public String toString() {
        return displayName;
    }
}