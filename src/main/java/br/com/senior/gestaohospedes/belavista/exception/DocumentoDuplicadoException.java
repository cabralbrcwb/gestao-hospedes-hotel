package br.com.senior.gestaohospedes.belavista.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DocumentoDuplicadoException extends BusinessException {

    public DocumentoDuplicadoException(String documento) {
        super("error.hospede.documentoDuplicado", documento);
    }
}
