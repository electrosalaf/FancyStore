package io.electrosalaf.fancystore.dto.checkout;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemDto {

    private String productName;
    private int quantity;
    private double price;
    private long productId;
}
