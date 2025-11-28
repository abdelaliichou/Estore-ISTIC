package estore.istic.fr.Facade;

public interface OnFavoriteProductsModifiedListener {
    void onLoading();
    void onSuccess(String message);
    void onError(String message);
}
