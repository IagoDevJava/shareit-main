package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Comparable<ItemDto> {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private List<CommentDto> comments;

    @Override
    public int compareTo(ItemDto itemDto) {
        return this.id.compareTo(itemDto.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDto)) return false;
        return id != null && id.equals(((ItemDto) obj).getId());
    }
}