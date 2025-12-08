package estore.istic.fr.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import estore.istic.fr.Facade.OnProductActionListener;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;
import estore.istic.fr.Resources.ProductsDiffCallback;
import estore.istic.fr.Resources.Utils;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_GRID = 1;
    OnProductActionListener productListener;
    List<ProductDto> productsList;
    Context context;
    boolean isGrid;

    public ProductsAdapter(
            Context context,
            OnProductActionListener listener,
            List<ProductDto> products,
            boolean isGrid
    ) {
        this.isGrid = isGrid;
        this.context = context;
        this.productsList = new ArrayList<>(products);
        this.productListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return isGrid ? VIEW_TYPE_GRID : VIEW_TYPE_LIST;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;
        if (viewType == VIEW_TYPE_GRID) {
            layout = R.layout.populair_item_gridlayout;
        } else {
            layout = R.layout.populair_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(
                layout,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            @SuppressLint("RecyclerView") int position
    ) {

        ProductDto product = productsList.get(position);
        Picasso.get().load(product.getProduct().getImageUrl())
                .into(holder.productImage);

        holder.productName.setText(product.getProduct().getName());
        holder.productPrice.setText(Utils.dollarFormatter.format(product.getProduct().getPrice()));

        holder.card.setAnimation(AnimationUtils.loadAnimation(
                context,
                R.anim.card_pop_up
        ));

        if (product.isFavorite()) {
            holder.fullHeart.setVisibility(View.VISIBLE);
            holder.emptyHeart.setVisibility(View.GONE);
        } else {
            holder.fullHeart.setVisibility(View.GONE);
            holder.emptyHeart.setVisibility(View.VISIBLE);
        }

        if (product.getProduct().getPrice() >= 600f) {
            holder.hot.setVisibility(View.VISIBLE);
        } else {
            holder.hot.setVisibility(View.GONE);
        }

        holder.moreInformationButton.setOnClickListener(v ->
                productListener.onProductClicked(product)
        );

        holder.card.setOnClickListener(v ->
                productListener.onProductClicked(product)
        );

        holder.emptyHeart.setOnClickListener(v -> {
            if (holder.emptyHeart.getVisibility() == View.VISIBLE) {
                productListener.onProductLiked(
                        product.getProduct()
                );
            }
        });

        holder.fullHeart.setOnClickListener(v -> {
            if (holder.fullHeart.getVisibility() == View.VISIBLE) {
                productListener.onProductDisliked(
                        product.getProduct()
                );
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ProductDto> newList) {
        DiffUtil.Callback callback = new ProductsDiffCallback(this.productsList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        this.productsList.clear();
        this.productsList.addAll(newList);

        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, fullHeart, emptyHeart, ratting, hot;
        TextView productName, productPrice;
        MaterialCardView card;
        RelativeLayout moreInformationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratting = itemView.findViewById(R.id.ratting);
            hot = itemView.findViewById(R.id.populaire);
            productImage = itemView.findViewById(R.id.product_image);
            fullHeart = itemView.findViewById(R.id.product_liked_fill);
            emptyHeart = itemView.findViewById(R.id.product_liked_empty);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            card = itemView.findViewById(R.id.card);
            moreInformationButton = itemView.findViewById(R.id.more_informations);
        }
    }
}
