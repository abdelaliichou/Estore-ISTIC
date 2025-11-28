package estore.istic.fr.Facade;

import java.util.List;

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Dto.ProductDto;

public interface OnCategoriesResultListener {
    void onLoading();
    void onSuccess(List<Category> categories);
    void onError(String message);
}
