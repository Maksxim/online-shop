package by.tms.project.exception;

import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="NotFoundException occurred")
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException() {
        log.error("NotFoundException handler executed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException handler executed");
        return ResponseEntity.badRequest().body(Optional.of(ex.getMessage()));

    }
    @ExceptionHandler(FileSizeException.class)
    public ResponseEntity handleFileSizeException(FileSizeException fse){
        return ResponseEntity.badRequest().body(Optional.of(fse.getMessage()));
    }
}
