package enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NoteCategory {
    HOME("Home", "rgb(255, 145, 0)"),      // Orange
    WORK("Work", "rgb(92, 107, 192)"),      // Purple
    PERSONAL("Personal", "rgb(50, 140, 160)"); // Teal

    private final String label;
    private final String rgbColor;

    NoteCategory(String label, String rgbColor) {
        this.label = label;
        this.rgbColor = rgbColor;
    }

    @JsonValue
    public String getLabel() { return label; }

    public String getRgbColor() { return rgbColor; }

    // Constant for completed notes based on your HTML (rgba(40, 46, 41, 0.6))
    public static final String COMPLETED_COLOR = "rgba(40, 46, 41, 0.6)";

    public static String getColorByLabel(String label, boolean isCompleted) {
        if (isCompleted) return COMPLETED_COLOR;
        for (NoteCategory cat : values()) {
            if (cat.label.equalsIgnoreCase(label)) return cat.rgbColor;
        }
        return "rgb(255, 255, 255)"; // Default
    }

    public static NoteCategory random() {
        NoteCategory[] categories = values();
        return categories[new java.util.Random().nextInt(categories.length)];
    }
}
