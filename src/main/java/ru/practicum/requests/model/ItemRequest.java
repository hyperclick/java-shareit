package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "item_requests")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    String description;
    @OneToOne
    @JoinColumn(name = "requester_id")
    User requester;
    LocalDate created;

}
