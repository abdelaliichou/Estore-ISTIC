package estore.istic.fr.Facade;

import java.util.List;

import estore.istic.fr.Model.Dto.ProductDto;

public interface OnGetProductsResultListener {
    void onLoading();
    void onSuccess(List<ProductDto> products);
    void onError(String message);
}
