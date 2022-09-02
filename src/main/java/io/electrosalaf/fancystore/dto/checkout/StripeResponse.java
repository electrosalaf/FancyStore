package io.electrosalaf.fancystore.dto.checkout;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StripeResponse {
    private String sessionId;
}
