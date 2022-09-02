package io.electrosalaf.fancystore.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemDto {

    private String productName;
    private int quantity;
    private double price;
    private long productId;
}
