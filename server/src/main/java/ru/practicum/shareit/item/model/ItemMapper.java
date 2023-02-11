package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    /**
     * Конвертирование Item в ItemDto
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId() != null ? item.getRequestId() : null)
                .build();
    }

    /**
     * Конвертирование Item в ItemRequestDto
     */
    public static ItemForResponseDto toItemForResponseDto(Item item) {
        return ItemForResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId() != null ? item.getRequestId() : null)
                .build();
    }

    /**
     * Конвертация списка Item в ItemDto
     */
    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(toItemDto(item));
        }

        return result;
    }

    /**
     * Конвертация списка Item в ItemRequestDto
     */
    public static List<ItemForResponseDto> mapToItemForResponseDto(Iterable<Item> items) {
        List<ItemForResponseDto> result = new ArrayList<>();

        for (Item item : items) {
            result.add(toItemForResponseDto(item));
        }

        return result;
    }
}