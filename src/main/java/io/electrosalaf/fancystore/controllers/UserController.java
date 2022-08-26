package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.dto.user.SignupDto;
import io.electrosalaf.fancystore.dto.user.SignupResponseDto;
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
    public SignupResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signup(signupDto);
    }
}
