package io.electrosalaf.fancystore.dto.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddToCartDto {

    private Integer id;
    private @NotNull Integer productId;
    private @NotNull Integer quantity;

}
