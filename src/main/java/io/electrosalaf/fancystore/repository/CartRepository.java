package io.electrosalaf.fancystore.repository;

import io.electrosalaf.fancystore.model.Cart;
import io.electrosalaf.fancystore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
}
