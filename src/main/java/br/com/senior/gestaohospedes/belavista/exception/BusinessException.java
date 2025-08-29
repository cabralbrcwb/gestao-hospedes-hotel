package br.com.senior.gestaohospedes.belavista.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    protected BusinessException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }
}
