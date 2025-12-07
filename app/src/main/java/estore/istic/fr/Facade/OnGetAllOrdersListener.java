package estore.istic.fr.Facade;

import java.util.List;

import estore.istic.fr.Model.Domain.Order;

public interface OnGetAllOrdersListener {
     void onLoading();
     void onSuccess(List<Order> orders);
     void onError(String message);
}
