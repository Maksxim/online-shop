package by.tms.project.services;

import by.tms.project.model.CartInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class ShoppingCartServiceTest {

    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        this.shoppingCartService = new ShoppingCartService();
    }

  /*  @Test
    public void getCartInfoFromSession_Success() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        CartInfo cartInfo = shoppingCartService.getCartInfoFromSession(request);


    }*/
}
