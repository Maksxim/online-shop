package by.tms.project.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
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

    public Order(int id, LocalDateTime dateTime, List<Item> items, boolean finished) {
        this.id = id;
        this.dateTime = dateTime;
        this.items = items;
        this.finished = finished;
    }

    public Order() {
        this.dateTime = LocalDateTime.now();
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
