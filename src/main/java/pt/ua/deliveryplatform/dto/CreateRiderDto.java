package pt.ua.deliveryplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRiderDto {
    @JsonProperty("name")
    private String name;
}
