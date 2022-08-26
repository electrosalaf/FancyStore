package io.electrosalaf.fancystore.service;

import io.electrosalaf.fancystore.model.AuthenticationToken;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    // Save the confirmation token
    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    // Get token of the user
    public AuthenticationToken getToken(User user) {
        return tokenRepository.findTokenByUser(user);
    }
}
