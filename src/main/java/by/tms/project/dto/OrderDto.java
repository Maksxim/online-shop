package by.tms.project.dto;

import by.tms.project.entities.Item;
import by.tms.project.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OrderDto {

    private Integer id;
    private LocalDateTime dateTime;
    private List<Item> items = new ArrayList<>();
    private boolean finished = false;
    private Integer itemsCount;

    public static OrderDto of(Order order) {
        List<Item> items = order.getItems();
        Integer itemsCount = 0;
        if(items != null){
            itemsCount = items.stream().map(Item::getCount).reduce(0, Integer::sum);
        }
        return new OrderDto(order.getId(), order.getDateTime(), order.getItems(), order.isFinished(), itemsCount);
    }
}
