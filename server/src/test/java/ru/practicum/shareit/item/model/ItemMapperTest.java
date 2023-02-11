package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponseDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {
    Item item;
    ItemDto itemDto;
    ItemForResponseDto itemForResponseDto;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(0L)
                .name("name")
                .description("description")
                .requestId(1L)
                .ownerId(2L)
                .available(true)
                .build();
        itemDto = ItemDto.builder()
                .id(0L)
                .name("name")
                .description("description")
                .requestId(1L)
                .available(true)
                .build();
        itemForResponseDto = ItemForResponseDto.builder()
                .id(0L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void toItemDto() {
        ItemDto expectedItemDto = ItemMapper.toItemDto(item);

        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void toItemForResponseDto() {
        ItemForResponseDto expectedItemForResponseDto = ItemMapper.toItemForResponseDto(item);

        assertEquals(expectedItemForResponseDto, itemForResponseDto);
    }

    @Test
    void mapToItemDto() {
        List<ItemDto> expectedList = new ArrayList<>();
        List<Item> listItem = new ArrayList<>();
        expectedList.add(itemDto);
        listItem.add(item);

        List<ItemDto> actualList = ItemMapper.mapToItemDto(listItem);
        assertEquals(expectedList, actualList);
    }

    @Test
    void mapToItemForResponseDto() {
        List<ItemForResponseDto> expectedList = new ArrayList<>();
        List<Item> listItem = new ArrayList<>();
        expectedList.add(itemForResponseDto);
        listItem.add(item);

        List<ItemForResponseDto> actualList = ItemMapper.mapToItemForResponseDto(listItem);
        assertEquals(expectedList, actualList);
    }
}