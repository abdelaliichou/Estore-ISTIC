package estore.istic.fr.Model.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import estore.istic.fr.Model.Domain.OrderItem;
import estore.istic.fr.Model.Dto.CartItem;

public class OrderMapper {

    public static List<OrderItem> cartToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(c -> new OrderItem(
                        c.getProduct(),
                        c.getQuantity()
                ))
                .collect(Collectors.toList());
    }

}
