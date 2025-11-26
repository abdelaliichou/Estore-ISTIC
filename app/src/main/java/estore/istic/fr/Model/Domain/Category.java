package estore.istic.fr.Model.Domain;

public class Category {
    String categoryID;
    String name;
    String imageUrl;

    public Category() {}

    public Category(
            String category,
            String image
    ) {
        this.name = category;
        this.imageUrl = image;
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
