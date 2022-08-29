package io.electrosalaf.fancystore.dto.cart;

import io.electrosalaf.fancystore.model.Cart;
import io.electrosalaf.fancystore.model.Product;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartItemDto {

    private Integer id;
    private @NotNull Integer quantity;
    private @NotNull Product product;

    public CartItemDto(Cart cart) {
        this.setId(cart.getId());
        this.setQuantity(cart.getQuantity());
        this.setProduct(cart.getProduct());
    }
}
