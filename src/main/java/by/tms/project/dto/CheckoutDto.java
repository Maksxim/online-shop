package by.tms.project.dto;

import by.tms.project.entities.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class CheckoutDto {

    @Size(min=3, max=128)
    @NotNull
    private String name;
    @Size(min=3, max=128)
    @NotNull
    private String email;
    @Size(min=3, max=128)
    @NotNull
    private String phone;
    @Size(min=3, max=128)
    @NotNull
    private String address;
    @NotNull
    private String paymentMethod;
}
