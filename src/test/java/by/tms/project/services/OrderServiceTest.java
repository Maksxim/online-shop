package by.tms.project.services;

import by.tms.project.dto.OrderDto;
import by.tms.project.entities.Item;
import by.tms.project.entities.Order;
import by.tms.project.entities.PaymentMethod;
import by.tms.project.exception.NotFoundException;
import by.tms.project.repositories.ItemRepository;
import by.tms.project.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private ProductService productService;
    private ItemRepository itemRepository;


    @BeforeEach
    public void before() {
        orderRepository = Mockito.mock(OrderRepository.class);
        productService = Mockito.mock(ProductService.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        orderService = new OrderService(orderRepository,productService,itemRepository);
    }

    @Test
    public void getOrder_Success() {
        Order order = testOrder();
        Mockito.when(orderRepository.findById(10)).thenReturn(Optional.of(order));

        Order actualOrder = orderService.getOrder(10);
        assertEquals(10, actualOrder.getId());
        assertEquals("address", actualOrder.getAddress());
        assertEquals("email", actualOrder.getEmail());
        assertEquals(true, actualOrder.isFinished());
        assertEquals("name", actualOrder.getName());
        assertEquals(PaymentMethod.CREDIT_CARD, actualOrder.getPaymentMethod());
        assertEquals("12345678", actualOrder.getPhone());
        assertEquals(order.getDateTime(), actualOrder.getDateTime());
        Item item = actualOrder.getItems().get(0);
        assertEquals(10, item.getId());
        assertEquals(4, item.getCount());
        assertEquals(200, item.getPrice());
        assertEquals(10, item.getProductId());
        assertEquals("name", item.getProductName());
    }

    @Test
    public void getOrder_NotFound(){
        Mockito.when(orderRepository.findById(9999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> orderService.getOrder(9999));
    }

    @Test
    public void createOrder_Success(){
        Order order = testOrder();
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        Order actualOrder = orderService.createOrder(order);

        assertEquals(10, actualOrder.getId());
        assertEquals("address", actualOrder.getAddress());
        assertEquals("email", actualOrder.getEmail());
        assertEquals(true, actualOrder.isFinished());
        assertEquals("name", actualOrder.getName());
        assertEquals(PaymentMethod.CREDIT_CARD, actualOrder.getPaymentMethod());
        assertEquals("12345678", actualOrder.getPhone());
        assertEquals(order.getDateTime(), actualOrder.getDateTime());
        Item item = actualOrder.getItems().get(0);
        assertEquals(10, item.getId());
        assertEquals(4, item.getCount());
        assertEquals(200, item.getPrice());
        assertEquals(10, item.getProductId());
        assertEquals("name", item.getProductName());
        assertEquals(order, item.getOrder());

        verify(itemRepository, only()).saveAll(order.getItems());
    }

    @Test
    public void getAll_Success(){
        Pageable pageRequest = PageRequest.of(0, 1);
        List<Order> list = new ArrayList<>();
        Order order = testOrder();
        list.add(order);
        Page<Order> page = new PageImpl<>(list, pageRequest, 100);
        Mockito.when(orderRepository.findAll(pageRequest)).thenReturn(page);

        Page<OrderDto> pageDto = orderService.getAll(pageRequest);

        List<OrderDto> listOrderDto = pageDto.getContent();
        assertEquals(1, listOrderDto.size());
        assertEquals(order.getId(), listOrderDto.get(0).getId());
        assertEquals(4, listOrderDto.get(0).getItemsCount());
        assertEquals(order.getDateTime(), listOrderDto.get(0).getDateTime());
        assertEquals(order.getItems(), listOrderDto.get(0).getItems());
    }

    private static Order testOrder(){
        LocalDateTime dateTime = LocalDateTime.now();
        Item item = Item.builder()
                .id(10)
                .count(4)
                .price(200)
                .productId(10)
                .productName("name")
                .build();
        List<Item> list = new ArrayList<>();
        list.add(item);

        Order order = Order.builder()
                .id(10)
                .dateTime(dateTime)
                .address("address")
                .email("email")
                .finished(true)
                .name("name")
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .phone("12345678")
                .items(list)
                .build();
        return order;
    }
}
