package estore.istic.fr.Facade;

import estore.istic.fr.Model.Dto.CartItem;

public interface OnCartAdapterListener {
    void onProductLongClicked(CartItem item);
}
