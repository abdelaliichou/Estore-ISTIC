package estore.istic.fr.Facade;

import java.util.List;

import estore.istic.fr.Model.Dto.ProductDto;

public interface OnFavoriteProductsResultListener {
    void onLoading();
    void onSuccess(String message);
    void onError(String message);
}
