package by.tms.project.model;

import by.tms.project.entities.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartInfo {

    List<CartItem> items = new ArrayList<>();

    public void addProduct(Product product, int count) {
        CartItem item = findByProductId(product.getId());
        if (item == null) {
            item = new CartItem(product, count);
            items.add(item);
        } else {
            int newCount = item.getCount() + count;
            if(newCount == 0){
                items.remove(item);
            } else {
                item.setCount(newCount);
            }
        }
    }

    private CartItem findByProductId(int productId) {
        return items.stream().filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
    }

    public int getTotalCount() {
        int total = 0;
        for (CartItem item : items) {
            total+=item.getCount();
        }
        return total;
    }

    public double getTotalPrice() {
        double result = 0;
        for(int i = 0; i < items.size(); i++){
            double price = items.get(i).getProduct().getPrice();
            double count = items.get(i).getCount();
            double total = price * count;
            result += total;
        }
        return result;
    }
}
