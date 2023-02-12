package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {

    LocalDateTime time;
    Comment comment;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();
        comment = Comment.builder()
                .id(0L)
                .text("text")
                .itemId(1L)
                .authorId(1L)
                .created(time)
                .build();
        commentDto = CommentDto.builder()
                .id(0L)
                .text("text")
                .itemId(1L)
                .created(time)
                .build();
    }

    @Test
    void toCommentDto() {
        CommentDto actualCommentDto = CommentMapper.toCommentDto(comment);

        assertEquals(commentDto, actualCommentDto);
    }

    @Test
    void mapToCommentDto() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        List<CommentDto> commentDtos = new ArrayList<>();
        commentDtos.add(commentDto);

        List<CommentDto> actualListCommentDtos = CommentMapper.mapToCommentDto(comments);

        assertEquals(commentDtos, actualListCommentDtos);
    }
}