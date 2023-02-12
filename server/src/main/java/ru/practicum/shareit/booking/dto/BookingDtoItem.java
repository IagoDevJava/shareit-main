package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoItem {
    private Long id;
    private Long bookerId;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BookingDtoItem)) return false;
        return id != null && id.equals(((BookingDtoItem) obj).getId());
    }
}
