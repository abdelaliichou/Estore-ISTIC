package estore.istic.fr.Facade;

import java.util.List;

import estore.istic.fr.Model.Dto.CartItem;

public interface OnCartRealTimeListener {
     void onLoading();
     void onData(List<CartItem> cartItems);
     void onError(String message);
}
