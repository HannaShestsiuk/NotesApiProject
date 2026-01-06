package records;

public record NoteWithStatus(
        String title,
        String description,
        boolean completed,
        String category
) {
    public NoteWithStatus {
        title = title == null ? null : title.trim();
        description = description == null ? null : description.trim();
    }
}
