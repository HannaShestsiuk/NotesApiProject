package enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NoteCategory {
    HOME("Home"),
    WORK("Work"),
    PERSONAL("Personal");

    private final String label;

    NoteCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
