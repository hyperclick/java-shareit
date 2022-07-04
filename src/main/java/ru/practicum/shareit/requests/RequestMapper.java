package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

public class RequestMapper {
    public static ItemRequestDto toRequestItemDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                (ItemRequestDto.User) itemRequest.getRequester(),/* не знаю правильно ли это*/
                itemRequest.getCreated()
        );
    }
}
