package by.tms.project.services;

import by.tms.project.model.CartInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ShoppingCartService {

    public CartInfo getCartInfoFromSession(HttpServletRequest request) {
        CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("cartInfo");
        if (cartInfo == null) {
            cartInfo = new CartInfo();
            request.getSession().setAttribute("cartInfo", cartInfo);
        }
        return cartInfo;
    }

    public void saveCartInfoToSession(HttpServletRequest request, CartInfo cartInfo) {
        request.getSession().setAttribute("cartInfo", cartInfo);
    }

    public void clear(HttpServletRequest request){
        request.getSession().removeAttribute("cartInfo");
    }
}
