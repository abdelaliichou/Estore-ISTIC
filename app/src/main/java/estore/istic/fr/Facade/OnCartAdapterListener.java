package estore.istic.fr.Facade;

import estore.istic.fr.Model.Domain.CartItem;

public interface OnCartAdapterListener {
    void onProductLongClicked(CartItem item);
}
