package records;

import enums.NoteCategory;

public record Note(
        String title,
        String description,
        String category
) {
    public Note {
        title = title == null ? null : title.trim();
        description = description == null ? null : description.trim();
    }
}
