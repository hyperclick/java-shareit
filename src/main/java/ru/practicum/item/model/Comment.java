package ru.practicum.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comments")
@AllArgsConstructor
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String text;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne
    @JoinColumn(name = "author_id")
    private User author;

    LocalDateTime created;
}
