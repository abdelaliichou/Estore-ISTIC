package estore.istic.fr.Model.Dto;

import estore.istic.fr.Model.Domain.Product;

public class ProductDto {
    Product product;
    boolean isFavorite;

    public ProductDto() {}
    public ProductDto(
            Product product,
            boolean isFavorite
    ) {
        this.product = product;
        this.isFavorite = isFavorite;
    }

    public Product getProduct() { return this.product; }
    public String getPriceString() { return "$" + product.getPrice(); }
    public boolean isFavorite() { return isFavorite; }
}
