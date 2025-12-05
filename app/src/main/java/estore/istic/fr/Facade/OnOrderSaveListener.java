package estore.istic.fr.Facade;

import java.util.Optional;

public interface OnOrderSaveListener {
     void onLoading();
     void onSuccess(String orderId);
     void onError(String message);
}
