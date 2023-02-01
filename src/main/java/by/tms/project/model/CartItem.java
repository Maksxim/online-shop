package by.tms.project.model;

import by.tms.project.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {

    private Product product;
    private Integer count;
}
