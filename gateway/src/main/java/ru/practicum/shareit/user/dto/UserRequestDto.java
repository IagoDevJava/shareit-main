package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserRequestDto)) return false;
        return id != null && id.equals(((UserRequestDto) obj).getId());
    }
}