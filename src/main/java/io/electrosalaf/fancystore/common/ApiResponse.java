package io.electrosalaf.fancystore.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse {

    private final boolean success;
    private final String message;

    public String getTimeStamp() {
        return LocalDateTime.now().toString();
    }
}
