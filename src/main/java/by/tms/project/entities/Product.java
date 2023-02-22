package by.tms.project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "product_name")
    @NotNull
    @Size(min=3, max=128)
    private String productName;

    @Column(name = "description")
    @NotNull
    @Size(min=3, max=512)
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.0",inclusive = false)
    private double price;

    @Column(name = "image_path")
    private String imagePath;

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
