package enums;

public enum NoteCategory {
    HOME("Home"),
    WORK("Work"),
    PERSONAL("Personal");

    private final String label;

    NoteCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
