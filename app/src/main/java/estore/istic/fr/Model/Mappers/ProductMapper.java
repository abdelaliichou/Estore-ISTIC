package estore.istic.fr.Model.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        return new ProductDto(product, false);
    }

    public static List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Product fromDto(ProductDto dto) {
        return dto.getProduct();
    }

    public static List<Product> fromDtoList(List<ProductDto> dtoList) {
        return dtoList.stream()
                .map(ProductMapper::fromDto)
                .collect(Collectors.toList());
    }
}
