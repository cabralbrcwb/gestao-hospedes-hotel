package br.com.senior.gestaohospedes.belavista.exception;

import br.com.senior.gestaohospedes.belavista.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Manipulador global de exceções para a aplicação.
 * Captura exceções específicas e as converte em respostas HTTP apropriadas.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  private String getMessage(BusinessException ex) {
    return messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
  }

  private ErrorResponseDTO createErrorResponse(HttpStatus status, String message, WebRequest request) {
    String description = request.getDescription(false);
    String path = description != null && description.startsWith("uri=") ? description.substring(4) : description;
    return new ErrorResponseDTO(status.value(), status.getReasonPhrase(), message, path);
  }

  @ExceptionHandler(HospedeComReservaAtivaException.class)
  public ResponseEntity<ErrorResponseDTO> handleHospedeComReservaAtiva(
      HospedeComReservaAtivaException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(createErrorResponse(HttpStatus.CONFLICT, getMessage(ex), request));
  }

  @ExceptionHandler(DocumentoDuplicadoException.class)
  public ResponseEntity<ErrorResponseDTO> handleDocumentoDuplicado(DocumentoDuplicadoException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(createErrorResponse(HttpStatus.CONFLICT, getMessage(ex), request));
  }

  @ExceptionHandler(ReservaSobrepostaException.class)
  public ResponseEntity<ErrorResponseDTO> handleReservaSobreposta(ReservaSobrepostaException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(createErrorResponse(HttpStatus.CONFLICT, getMessage(ex), request));
  }

  @ExceptionHandler(ReservaStatusException.class)
  public ResponseEntity<ErrorResponseDTO> handleReservaStatus(ReservaStatusException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(createErrorResponse(HttpStatus.BAD_REQUEST, getMessage(ex), request));
  }

  @ExceptionHandler(CheckInForaDoHorarioException.class)
  public ResponseEntity<ErrorResponseDTO> handleCheckInForaDoHorario(
      CheckInForaDoHorarioException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(createErrorResponse(HttpStatus.BAD_REQUEST, getMessage(ex), request));
  }

  @ExceptionHandler(DataInvalidaException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataInvalida(DataInvalidaException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(createErrorResponse(HttpStatus.BAD_REQUEST, getMessage(ex), request));
  }

  @ExceptionHandler(CancelamentoInvalidoException.class)
  public ResponseEntity<ErrorResponseDTO> handleCancelamentoInvalido(
      CancelamentoInvalidoException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(createErrorResponse(HttpStatus.BAD_REQUEST, getMessage(ex), request));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    String message = messageSource.getMessage("entity.not.found", null, LocaleContextHolder.getLocale());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(createErrorResponse(HttpStatus.NOT_FOUND, message, request));
  }

}
