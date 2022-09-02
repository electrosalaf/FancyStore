package io.electrosalaf.fancystore.service;

import io.electrosalaf.fancystore.dto.cart.AddToCartDto;
import io.electrosalaf.fancystore.dto.cart.CartDto;
import io.electrosalaf.fancystore.dto.cart.CartItemDto;
import io.electrosalaf.fancystore.exceptions.CartItemNotExistException;
import io.electrosalaf.fancystore.model.Cart;
import io.electrosalaf.fancystore.model.Product;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addToCart(AddToCartDto addToCartDto, Product product, User user) {
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }

    public CartDto listCartItems(User user) {

        // Get all cart items for a user
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        // Convert cart to cartItemDto
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart : cartList) {
            cartItems.add(new CartItemDto(cart));
        }

        // calculate total price
        double totalCost = 0;
        for (CartItemDto cartItemDto : cartItems) {
            totalCost += cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity();
        }

        // return cart DTO
        return new CartDto(cartItems, totalCost);
    }

    public void deleteCartItem(int cartItem, User user) throws CartItemNotExistException {

        Optional<Cart> optionalCart = cartRepository.findById(cartItem);


        if (!optionalCart.isPresent()) {
            throw new CartItemNotExistException("Item not found");
        }

        Cart cart = optionalCart.get();
        if (cart.getUser() != user) {
            throw new CartItemNotExistException("This cart item does not belong to user");
        }

        cartRepository.deleteById(cartItem);
    }
}
