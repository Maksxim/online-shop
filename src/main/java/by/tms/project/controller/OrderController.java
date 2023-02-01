package by.tms.project.controller;

import by.tms.project.dto.CheckoutDto;
import by.tms.project.dto.OrderDto;
import by.tms.project.entities.Order;
import by.tms.project.entities.Product;
import by.tms.project.model.CartInfo;
import by.tms.project.services.OrderService;
import by.tms.project.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service){
        this.service = service;
    }

    @GetMapping("/admin/order")
    public String showOrderList(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<OrderDto> orderPage = service.getAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("orders", orderPage);

        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "orders";
    }



    @GetMapping("/admin/order/{id}")
    public String getOrderPage(@PathVariable("id") int id, Model model) {
        Order order = service.getOrder(id);
        model.addAttribute("order", order);

        return "admin-view-order";
    }
}
