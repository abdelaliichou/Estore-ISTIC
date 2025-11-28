package estore.istic.fr.Facade;

import java.util.ArrayList;

import estore.istic.fr.Model.Dto.ProductDto;

public interface OnProductsResultListener {
    void onLoading();
    void onSuccess(ArrayList<ProductDto> products);
    void onError(String message);
}
