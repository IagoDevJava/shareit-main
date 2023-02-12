package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long id;
    @NotEmpty
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentRequestDto that = (CommentRequestDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(text, that.text)
                && Objects.equals(itemId, that.itemId)
                && Objects.equals(authorName, that.authorName)
                && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, itemId, authorName, created);
    }
}
