package io.electrosalaf.fancystore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInDtoResponse {
    private String status;
    private String token;
}
