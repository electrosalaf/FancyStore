package io.electrosalaf.fancystore.repository;

import io.electrosalaf.fancystore.model.Order;
import io.electrosalaf.fancystore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);
}
