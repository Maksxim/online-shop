package by.tms.project.services;

import by.tms.project.Logger;
import by.tms.project.dto.OrderDto;
import by.tms.project.entities.Item;
import by.tms.project.entities.Order;
import by.tms.project.entities.Product;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ItemRepository;
import by.tms.project.repositories.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Order createOrder(Order order){
        Order createdOrder = orderRepository.save(order);
        order.getItems().forEach(item -> item.setOrder(order));
        itemRepository.saveAll(order.getItems());
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
    @Logger
    public Page<OrderDto> getAll(Pageable pageable){
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        List<Order> content = ordersPage.getContent();
        List<OrderDto> newContent = new ArrayList<>();
        if (!content.isEmpty()) {
            newContent = content.stream().map(OrderDto::of).collect(Collectors.toList());
        }
        return new PageImpl<>(newContent, ordersPage.getPageable(), ordersPage.getTotalElements());
    }
}

