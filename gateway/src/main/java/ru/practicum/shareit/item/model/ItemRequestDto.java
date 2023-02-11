package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    public void setId(Long id) {
        if (id != null) {
            this.id = id;
        }
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setAvailable(Boolean available) {
        if (available != null) {
            this.available = available;
        }
    }

    public void setOwnerId(Long ownerId) {
        if (ownerId != null) {
            this.ownerId = ownerId;
        }
    }

    public void setRequestId(Long requestId) {
        if (requestId != null) {
            this.requestId = requestId;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemRequestDto)) return false;
        return id != null && id.equals(((ItemRequestDto) obj).getId());
    }
}