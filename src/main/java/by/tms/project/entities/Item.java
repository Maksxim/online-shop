package by.tms.project.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "price")
    private double price;
    @Column(name = "count")
    private int count;

    public Item(Integer id, int productId, double price, int count) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.count = count;
    }

    public Item() {
    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", productId=" + productId +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
