package io.electrosalaf.fancystore.repository;

import io.electrosalaf.fancystore.model.AuthenticationToken;
import io.electrosalaf.fancystore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {

    AuthenticationToken findTokenByUser(User user);
    AuthenticationToken findTokenByToken(String token);
    

}
