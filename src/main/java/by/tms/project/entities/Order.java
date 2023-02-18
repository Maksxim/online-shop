package by.tms.project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Item> items = new ArrayList<>();
    @Column(name = "finished")
    private boolean finished = false;
    @Column(name = "name")
    @Size(min=3, max=128)
    private String name;
    @Column(name = "email")
    @Size(min=3, max=128)
    private String email;
    @Column(name = "phone")
    @Size(min=3, max=128)
    private String phone;
    @Column(name = "address")
    @Size(min=3, max=128)
    private String address;
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    public Order() {
        this.dateTime = LocalDateTime.now();
    }

    public double getTotalPrice() {
        double result = 0;
        for(int i = 0; i < items.size(); i++){
            double price = items.get(i).getPrice();
            double count = items.get(i).getCount();
            double total = price * count;
            result += total;
        }
        return result;
    }

    @Override
    public String toString() {
        return " Order{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", items=" + items +
                ", finished=" + finished +
                '}';
    }
}
