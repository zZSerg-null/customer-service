package ru.customer.petproject.customerservice.unittests.handler;

import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.globalexeptionsspringbootstarter.exception.ResourceNotFoundException;
import ru.zinovievbank.customerservice.handler.GrpcExceptionAdvice;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GrpcExceptionHandlerTest {

    @InjectMocks
    private GrpcExceptionAdvice grpcExceptionHandler;

    @Test
    @DisplayName("Catch resource not found exception test")
    void testCatchResourceNotFoundException() {
        var ex = new ResourceNotFoundException();
        var statusException = grpcExceptionHandler.handleResourceNotFoundException(ex);
        assertEquals(Status.NOT_FOUND.getCode(), statusException.getStatus().getCode());
    }

    @Test
    @DisplayName("Catch invalid argument exception test")
    void testCatchInvalidArgumentException() {
        var ex = new IllegalArgumentException();
        var statusException = grpcExceptionHandler.handleInvalidArgument(ex);
        assertEquals(Status.INVALID_ARGUMENT.getCode(), statusException.getStatus().getCode());
    }

    @Test
    @DisplayName("Catch other exception test")
    void testCatchOtherException() {
        var statusException = grpcExceptionHandler.handleOtherException();
        assertEquals(Status.INTERNAL.getCode(), statusException.getStatus().getCode());
    }
}
