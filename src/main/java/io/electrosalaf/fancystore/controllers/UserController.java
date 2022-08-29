package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.dto.user.SignInDto;
import io.electrosalaf.fancystore.dto.user.SignInResponseDto;
import io.electrosalaf.fancystore.dto.user.SignUpDto;
import io.electrosalaf.fancystore.dto.user.SignupResponseDto;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.exceptions.CustomException;
import io.electrosalaf.fancystore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignUpDto signupDto) throws CustomException {
        return userService.signup(signupDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) throws AuthenticationFailException, CustomException {
        return userService.signIn(signInDto);
    }
}
