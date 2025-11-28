package estore.istic.fr.Facade;

import estore.istic.fr.Model.Dto.ProductDto;

public interface OnCategoryActionListener {
    void onProductClicked(ProductDto product);
    void onProductLiked(ProductDto product, int position);
    void onProductUnliked(ProductDto product, int position);
}
