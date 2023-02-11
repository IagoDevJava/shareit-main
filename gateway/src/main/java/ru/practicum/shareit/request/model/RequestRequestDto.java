package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.exception.RequestDescriptionIsNullException;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestRequestDto {
    private long id;
    private String description;
    private Long requesterID;
    private LocalDateTime created;

    public long getId() {
        return id;
    }

    public String getDescription() {
        if (description != null) {
            return description;
        } else {
            throw new RequestDescriptionIsNullException("Description is null");
        }
    }

    public Long getRequesterID() {
        return requesterID;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestRequestDto that = (RequestRequestDto) o;
        return id == that.id && Objects.equals(description, that.description)
                && Objects.equals(requesterID, that.requesterID)
                && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterID, created);
    }
}
