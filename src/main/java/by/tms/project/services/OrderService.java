package by.tms.project.services;

import by.tms.project.Logger;
import by.tms.project.entities.Item;
import by.tms.project.entities.Order;
import by.tms.project.entities.Product;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ItemRepository;
import by.tms.project.repositories.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(OrderService.class);

    private OrderRepository orderRepository;
    private ProductService productService;
    private ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, ItemRepository itemRepository){
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.itemRepository = itemRepository;
    }

    @Logger
    public Order createOrder(){
        Order order = new Order();
        Order createdOrder = orderRepository.save(order);
        return createdOrder;
    }

    @Logger
    @Transactional
    public void addItem(int orderId, int productId, int count){
        Item item = new Item();
        item.setCount(count);
        Product product = productService.getProduct(productId);
        if(product == null) {
            log.error("OrderService: Product id={} not found.", productId);
            return;
        }
        item.setProductId(product.getId());
        item.setPrice(product.getPrice());


        Order order = getOrder(orderId);
        if(order == null) {
            log.error("OrderService: Order id={} not found.", orderId);
            return;
        }
        item.setOrder(order);
        itemRepository.save(item);
        order.getItems().add(item);
        orderRepository.save(order);
    }

    @Logger
    public void finishOrder(int orderId) {
       Order order = getOrder(orderId);
       if (order == null) {
           log.error("OrderService: Order not found.");
         return;
       }
        order.setFinished(true);
        order.setDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Logger
    public Order getOrder(int orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()){
            throw new NotFoundException("order not found");
        }
        return order.get();
    }
}
