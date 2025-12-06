package estore.istic.fr.Facade;

import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;

public interface OnProductActionListener {
    void onProductClicked(ProductDto product);
    void onProductLiked(Product product);
    void onProductDisliked(Product product);
}
