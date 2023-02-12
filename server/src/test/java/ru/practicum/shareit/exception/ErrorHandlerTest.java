package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {
    ErrorHandler errorHandler;
    String message;
    ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
        message = "I am exception";
    }

    @Test
    void handleValidationException() {
        ValidationException expected = new ValidationException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleValidationException(new ValidationException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleNotFoundUserException() {
        UserNotFoundException expected = new UserNotFoundException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleNotFoundUserException(new UserNotFoundException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleNotFoundItemException() {
        ItemNotFoundException expected = new ItemNotFoundException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleNotFoundItemException(new ItemNotFoundException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleNotFoundBookingException() {
        BookingNotFoundException expected = new BookingNotFoundException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleNotFoundBookingException(new BookingNotFoundException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleThrowable() {
        Throwable expected = new Throwable(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleThrowable(new Throwable(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleOwnerNotFoundException() {
        OwnerNotFoundException expected = new OwnerNotFoundException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleOwnerNotFoundException(new OwnerNotFoundException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleItemAvailabilityException() {
        ItemAvailabilityException expected = new ItemAvailabilityException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleItemAvailabilityException(new ItemAvailabilityException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleBookingStartDateTimeException() {
        BookingStartDateTimeException expected = new BookingStartDateTimeException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleBookingStartDateTimeException(new BookingStartDateTimeException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleStateNotFoundException() {
        StateNotCurrentException expected = new StateNotCurrentException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleStateNotCurrentException(new StateNotCurrentException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleBookingChangeApprovedStatus() {
        BookingChangeApprovedStatusException expected = new BookingChangeApprovedStatusException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleBookingChangeApprovedStatus(new BookingChangeApprovedStatusException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleBookingCreateOwnerException() {
        BookingCreateOwnerException expected = new BookingCreateOwnerException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleBookingCreateOwnerException(new BookingCreateOwnerException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleAddCommentWithoutBookingException() {
        AddCommentWithoutBookingException expected = new AddCommentWithoutBookingException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleAddCommentWithoutBookingException(new AddCommentWithoutBookingException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleAddEmptyComment() {
        AddEmptyCommentException expected = new AddEmptyCommentException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleAddEmptyComment(new AddEmptyCommentException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleRequestDescriptionIsNullException() {
        RequestDescriptionIsNullException expected = new RequestDescriptionIsNullException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleRequestDescriptionIsNullException(new RequestDescriptionIsNullException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }

    @Test
    void handleRequestNotFoundException() {
        RequestNotFoundException expected = new RequestNotFoundException(message);
        errorResponse = new ErrorResponse(expected.getMessage());
        ErrorResponse actualError = errorHandler.handleRequestNotFoundException(new RequestNotFoundException(message));

        assertEquals(errorResponse.getError(), actualError.getError());
    }
}