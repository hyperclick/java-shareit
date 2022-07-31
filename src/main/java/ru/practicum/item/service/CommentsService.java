package ru.practicum.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.item.CommentMapper;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.CommentsRepository;
import ru.practicum.user.storage.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;


    public CommentsService(CommentsRepository commentsRepository, UserRepository userRepository) {
        this.commentsRepository = commentsRepository;

        this.userRepository = userRepository;
    }

    public Comment addNew(Item item, CommentDto dto, int authorId) {

        var model = CommentMapper.toModel(dto, item, userRepository.getReferenceById(authorId));
//        validate(model);
        return commentsRepository.saveAndFlush(model);
    }


    public Collection<Comment> getForItem(int itemId) {
        return commentsRepository
                .findAll()
                .stream()
                .filter(c -> c.getItem().getId() == itemId)
                .collect(Collectors.toList());
    }
}
