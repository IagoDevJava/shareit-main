package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDto that = (RequestDto) o;
        return id == that.id
                && Objects.equals(description, that.description)
                && Objects.equals(requesterId, that.requesterId)
                && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterId, created);
    }
}
