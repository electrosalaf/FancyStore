package io.electrosalaf.fancystore.dto.user;

import lombok.Data;

@Data
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
