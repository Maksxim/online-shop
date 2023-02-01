package by.tms.project.controller;

import by.tms.project.dto.CheckoutDto;
import by.tms.project.entities.Item;
import by.tms.project.entities.Order;
import by.tms.project.entities.PaymentMethod;
import by.tms.project.entities.Product;
import by.tms.project.model.CartInfo;
import by.tms.project.model.CartItem;
import by.tms.project.services.OrderService;
import by.tms.project.services.ProductService;
import by.tms.project.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ShoppingCartController {

    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    public ShoppingCartController(ProductService productService, ShoppingCartService shoppingCartService, OrderService orderService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(HttpServletRequest request, Model model){
        CartInfo cartInfo = shoppingCartService.getCartInfoFromSession(request);
        model.addAttribute("cartInfo", cartInfo);
        model.addAttribute("checkoutDto", new CheckoutDto());
        return "checkout";
    }

    @GetMapping("/addProductToCart/{id}")
    public String addProductToCart(HttpServletRequest request, Model model, @PathVariable("id") Integer id, @RequestParam(value = "count", required = false) Integer count){
        CartInfo cartInfo = shoppingCartService.getCartInfoFromSession(request);
        Product product = productService.getProduct(id);
        cartInfo.addProduct(product, count != null ? count : 1);
        shoppingCartService.saveCartInfoToSession(request, cartInfo);
        model.addAttribute("cartInfo", cartInfo);
        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping("/checkout")
    public String checkout(@Valid CheckoutDto checkoutDto, BindingResult result, Model model, HttpServletRequest request){
        if (result.hasErrors()) {
            model.addAttribute("cartInfo", shoppingCartService.getCartInfoFromSession(request));
            return "checkout";
        }
        Order order = new Order();
        CartInfo cartInfo = shoppingCartService.getCartInfoFromSession(request);
        List<Item> list = cartInfo.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            Item item = new Item();
            item.setProductId(product.getId());
            item.setCount(cartItem.getCount());
            item.setPrice(product.getPrice());
            item.setProductName(product.getProductName());
            return item;
        }).toList();

        order.setItems(list);
        order.setName(checkoutDto.getName());
        order.setEmail(checkoutDto.getEmail());
        order.setPhone(checkoutDto.getPhone());
        order.setAddress(checkoutDto.getAddress());
        order.setPaymentMethod(PaymentMethod.valueOf(checkoutDto.getPaymentMethod()));
        orderService.createOrder(order);
        shoppingCartService.clear(request);
        model.addAttribute("cartInfo", shoppingCartService.getCartInfoFromSession(request));
        return "checkout-success";
    }

}
