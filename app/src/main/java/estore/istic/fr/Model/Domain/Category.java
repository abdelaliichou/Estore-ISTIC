package estore.istic.fr.Model.Domain;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class Category implements Serializable {
    @PropertyName("category_id")
    String categoryId;
    String name;
    @PropertyName("image_url")
    String imageUrl;

    public Category() {}

    public Category(
            String category,
            String image
    ) {
        this.name = category;
        this.imageUrl = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
