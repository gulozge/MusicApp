package com.atmosware.musicapp.core.utils.results;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionResult<T> {
    private LocalDateTime timestamp;
    private String type;
    private T message;

    public ExceptionResult(String type, T message) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.message = message;
    }
}
