package io.electrosalaf.fancystore.service;

import io.electrosalaf.fancystore.config.MessageStrings;
import io.electrosalaf.fancystore.dto.user.SignInDto;
import io.electrosalaf.fancystore.dto.user.SignInResponseDto;
import io.electrosalaf.fancystore.dto.user.SignUpDto;
import io.electrosalaf.fancystore.dto.user.SignupResponseDto;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.exceptions.CustomException;
import io.electrosalaf.fancystore.model.AuthenticationToken;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    // Check if current email address is registered
    public SignupResponseDto signup(SignUpDto signupDto) throws CustomException {

        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // throw exception if registered
            throw new CustomException("User already exists");
        }

        // encrypting the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword);

        try {
            userRepository.save(user);
            // success in creating
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(user);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            return new SignupResponseDto("success", "user created successfully");
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        byte[] digest = md5.digest();
        String hashes = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hashes;
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
        // find user by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if (!Objects.nonNull(user)) {
            throw new AuthenticationFailException("User does not exist");
        }

        // check if the password is right
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                // password do not match
                throw new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if (!Objects.nonNull(token)) {
            throw new CustomException(MessageStrings.AUTH_TOKEN_NOT_PRESENT);
        }

        return new SignInResponseDto("success", token.getToken());
    }
}
