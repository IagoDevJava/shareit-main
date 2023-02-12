package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public RequestServiceImpl(UserRepository userRepository,
                              RequestRepository requestRepository,
                              ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * добавить новый запрос вещи
     */
    @Override
    public RequestDto addRequest(Long userId, Request request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        request.getDescription();

        request.setRequesterID(userId);
        request.setCreated(LocalDateTime.now().withNano(0));

        Request saveRequest = requestRepository.save(request);
        RequestDto requestDto = RequestMapper.toRequestDto(saveRequest);
        log.info("Request was added");
        return requestDto;
    }

    /**
     * получить список своих запросов вместе с данными об ответах на них
     */
    @Override
    public List<RequestWithResponsesDto> getYourRequestsWithData(Long userId) {
        List<RequestWithResponsesDto> result = new ArrayList<>();
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        List<Request> requestByRequesterID = requestRepository.findRequestByRequesterID(userId);

        for (Request request : requestByRequesterID) {
            List<ItemForResponseDto> responsesForRequest = getResponsesForRequest(request);
            RequestWithResponsesDto requestWithResponseDto = RequestMapper.toRequestWithResponsesDto(request);
            requestWithResponseDto.setItems(responsesForRequest);
            result.add(requestWithResponseDto);
        }

        Collections.sort(result);
        return result;
    }

    /**
     * получить список запросов, созданных другими пользователями
     */
    @Override
    public List<RequestWithResponsesDto> getAllRequests(Long userId, Long from, Long size) {
        List<Request> requestList = new ArrayList<>();
        List<RequestWithResponsesDto> resultList = new ArrayList<>();
        Pageable pageable = PageRequest.of(
                Math.toIntExact(from),
                Math.toIntExact(size),
                Sort.by("created").descending());

        do {
            Page<Request> allRequest = requestRepository.findAll(pageable);
            requestList.addAll(allRequest.getContent());
            if (allRequest.hasNext()) {
                pageable = PageRequest.of(allRequest.getNumber() + 1, allRequest.getSize(), allRequest.getSort());
            } else {
                pageable = null;
            }
        } while (pageable != null);

        List<Request> collect = requestList.stream()
                .filter(request -> !Objects.equals(request.getRequesterID(), userId))
                .collect(Collectors.toList());

        for (Request request : collect) {
            List<ItemForResponseDto> responsesForRequest = getResponsesForRequest(request);
            RequestWithResponsesDto requestWithResponseDto = RequestMapper.toRequestWithResponsesDto(request);
            requestWithResponseDto.setItems(responsesForRequest);
            resultList.add(requestWithResponseDto);
        }

        return resultList;
    }

    /**
     * получить данные об одном конкретном запросе вместе с данными об ответах на него
     */
    @Override
    public RequestWithResponsesDto findRequestById(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Request requestById = requestRepository
                .findById(requestId).orElseThrow(() -> new RequestNotFoundException("Request not found"));

        List<ItemForResponseDto> responsesForRequest = getResponsesForRequest(requestById);
        RequestWithResponsesDto requestWithResponsesDto = RequestMapper.toRequestWithResponsesDto(requestById);
        requestWithResponsesDto.setItems(responsesForRequest);

        return requestWithResponsesDto;
    }

    /**
     * получение списка ответов к запросу
     */
    private List<ItemForResponseDto> getResponsesForRequest(Request request) {
        List<ItemForResponseDto> responses = new ArrayList<>();
        String[] keyWords = request.getDescription().split(" ");

        for (String word : keyWords) {
            List<Item> itemsByRequest = itemRepository.findItemsByRequest(word);
            if (!itemsByRequest.isEmpty()) {
                List<ItemForResponseDto> itemForResponseDtos = ItemMapper.mapToItemForResponseDto(itemsByRequest);
                responses.addAll(itemForResponseDtos);
            }
        }

        return responses.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
