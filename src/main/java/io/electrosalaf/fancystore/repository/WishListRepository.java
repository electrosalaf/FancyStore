package io.electrosalaf.fancystore.repository;

import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

    // find all products saved to wishlist by a user
    // sort by latest created date
    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);
}
