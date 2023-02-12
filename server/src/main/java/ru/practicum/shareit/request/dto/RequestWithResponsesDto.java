package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemForResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestWithResponsesDto implements Comparable<RequestWithResponsesDto> {
    private long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemForResponseDto> items;

    @Override
    public int compareTo(RequestWithResponsesDto o) {
        return this.created.compareTo(o.created);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestWithResponsesDto that = (RequestWithResponsesDto) o;
        return id == that.id
                && Objects.equals(description, that.description)
                && Objects.equals(requesterId, that.requesterId)
                && Objects.equals(created, that.created)
                && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterId, created, items);
    }
}
