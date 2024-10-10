package ru.zinovievbank.customerservice.handler;

import io.grpc.Status;
import io.grpc.StatusException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import ru.aston.globalexeptionsspringbootstarter.exception.ResourceNotFoundException;
@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

    /**
     * Handle {@link IllegalArgumentException}
     *
     * @return INVALID_ARGUMENT Status
     */
    @GrpcExceptionHandler
    public StatusException handleInvalidArgument(IllegalArgumentException e) {
        var status = Status.INVALID_ARGUMENT
            .withDescription("The client specified an invalid argument.")
            .withCause(e);
        log.error("Error, invalid argument");
        return status.asException();
    }

    /**
     * Handle {@link ResourceNotFoundException}
     *
     * @return NOT_FOUND StatusException
     */
    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusException handleResourceNotFoundException(ResourceNotFoundException e) {
        var status = Status.NOT_FOUND
            .withDescription("Some requested entity (e.g., file or directory) was not found.")
            .withCause(e);
        log.error("Error, resource not found");
        return status.asException();
    }

    /**
     * Handle other exceptions
     *
     * @return INTERNAL StatusException
     */
    @GrpcExceptionHandler(Exception.class)
    public StatusException handleOtherException() {
        log.error("Internal errors. This error code is reserved for serious errors.");
        return Status.INTERNAL
            .withDescription("Internal errors. This error code is reserved for serious errors.")
            .asException();
    }
}
