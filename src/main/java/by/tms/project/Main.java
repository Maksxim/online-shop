package by.tms.project;

import by.tms.project.entities.Order;
import by.tms.project.services.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        OrderService orderService = context.getBean(OrderService.class);
        Order order = orderService.createOrder();
        orderService.addItem(order.getId(),4,3);
    }
}