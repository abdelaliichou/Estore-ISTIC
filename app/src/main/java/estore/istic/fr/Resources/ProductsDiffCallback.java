package estore.istic.fr.Resources;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

import estore.istic.fr.Model.Dto.ProductDto;

public class ProductsDiffCallback extends DiffUtil.Callback {

    private final List<ProductDto> oldList;
    private final List<ProductDto> newList;

    public ProductsDiffCallback(List<ProductDto> oldList, List<ProductDto> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() { return oldList.size(); }

    @Override
    public int getNewListSize() { return newList.size(); }

    @Override
    public boolean areItemsTheSame(int oldPos, int newPos) {
        return oldList.get(oldPos).getProduct().getProductId()
                .equals(newList.get(newPos).getProduct().getProductId());
    }

    @Override
    public boolean areContentsTheSame(int oldPos, int newPos) {
        ProductDto oldItem = oldList.get(oldPos);
        ProductDto newItem = newList.get(newPos);

        // return false if favorite changed, true otherwise
        return oldItem.isFavorite() == newItem.isFavorite()
                && Objects.equals(oldItem.getProduct().getPrice(), newItem.getProduct().getPrice());
    }
}
