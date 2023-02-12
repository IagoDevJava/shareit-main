package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.exception.RequestDescriptionIsNullException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "request")
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column(unique = true)
    private String description;
    @Column(name = "requester_id")
    private Long requesterID;
    @Column
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
        Request that = (Request) o;
        return id == that.id && Objects.equals(description, that.description)
                && Objects.equals(requesterID, that.requesterID)
                && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requesterID, created);
    }
}
