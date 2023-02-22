package by.tms.project.controller;

import by.tms.project.config.SecurityConfig;
import by.tms.project.entities.Product;
import by.tms.project.model.CartInfo;
import by.tms.project.services.ProductService;
import by.tms.project.services.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private ShoppingCartService shoppingCartService;

    @WithMockUser(value = "admin")
    @Test
    public void adminRootPageTest() throws Exception {
        this.mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin/product"));
    }

    @Test
    public void adminProductsPageTest() throws Exception {
        when(productService.getAll(PageRequest.of(0, 10))).thenReturn(getPageProducts(10, 110));

        this.mockMvc.perform(get("/admin/product").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("pageNumbers", hasSize(11)));
    }

    @Test
    public void adminProductsPageTest_ForbiddenForNonAdmin() throws Exception {
        when(productService.getAll(PageRequest.of(0, 10))).thenReturn(getPageProducts(10, 110));

        this.mockMvc.perform(get("/admin/product").with(user("user1").roles("OTHER")))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @WithAnonymousUser
    @Test
    public void clientProductsPageTest() throws Exception {
        when(productService.getAll(PageRequest.of(0, 10))).thenReturn(getPageProducts(10, 110));
        CartInfo emptyCart = new CartInfo();
        when(shoppingCartService.getCartInfoFromSession(any())).thenReturn(new CartInfo());

        this.mockMvc.perform(get("/product").with(anonymous()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("pageNumbers", hasSize(11)))
                .andExpect(model().attribute("cartInfo", emptyCart));

        verify(shoppingCartService, only()).getCartInfoFromSession(any());
    }

    @Test
    public void deleteProductTest() throws Exception {
        doNothing().when(productService).delete(10);

        this.mockMvc.perform(get("/admin/product/delete/10").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().is(302));

        verify(productService, only()).delete(10);
    }

    private Page<Product> getPageProducts(int count, int total) {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(Product.builder().id(i).productName("product" + i).price(100.0).build());
        }
        return new PageImpl<>(list, Pageable.unpaged(), total);
    }
}
