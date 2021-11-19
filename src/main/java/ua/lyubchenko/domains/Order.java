package ua.lyubchenko.domains;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private Long id;
    private long petId;
    private long quantity;
    private String shipDate;
    private String status;
    private boolean complete;

}
