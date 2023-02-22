package by.tms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductDto {

    private Integer id;
    @NotNull
    @Size(min=3, max=128)
    private String productName;
    @NotNull
    @Size(min=3, max=512)
    private String description;
    @DecimalMin(value = "0.0",inclusive = false)
    private double price;
}
