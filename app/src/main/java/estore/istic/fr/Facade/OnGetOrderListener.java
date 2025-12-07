package estore.istic.fr.Facade;

import java.util.Optional;
import estore.istic.fr.Model.Domain.Order;

public interface OnGetOrderListener {
     void onLoading();
     void onSuccess(Optional<Order> order);
     void onError(String message);
}
