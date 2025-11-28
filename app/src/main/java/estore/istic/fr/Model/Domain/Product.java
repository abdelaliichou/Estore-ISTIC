package estore.istic.fr.Model.Domain;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import estore.istic.fr.Model.Dto.ProductDto;

public class Product implements Serializable {

    @PropertyName("product_id")
    String productId;
    @PropertyName("category_id")
    String categoryId;
    @PropertyName("image_url")
    String imageUrl;
    String name;
    Double price ;

    public Product() {}

    public Product(
            String name,
            double price,
            String imageUrl,
            String categoryId
    ) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Product(
            String name,
            double price,
            String imageUrl
    ) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
