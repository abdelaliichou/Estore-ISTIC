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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import estore.istic.fr.Facade.OnFavoriteProductsModifiedResultListener;
import estore.istic.fr.Facade.OnProductActionListener;
import estore.istic.fr.Model.Dto.ProductDto;
import estore.istic.fr.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements OnFavoriteProductsModifiedResultListener {

    OnProductActionListener productListener;

    List<ProductDto> productsList;
    Context context;

    public ProductsAdapter(
            Context context,
            OnProductActionListener listener,
            List<ProductDto> products
    ) {
        this.context = context;
        this.productsList = products;
        this.productListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.populair_item,
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
        holder.productPrice.setText("$".concat(product.getProduct().getPrice().toString()));
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
                        product.getProduct(),
                        position
                );
            }
        });

        holder.fullHeart.setOnClickListener(v -> {
            if (holder.fullHeart.getVisibility() == View.VISIBLE) {
                productListener.onProductUnliked(
                        product.getProduct(),
                        position
                );
            }
        });
    }

    @Override
    public void onProductAddedToFavorite(int position, boolean isFavorite) {
        productsList.get(position).setFavorite(isFavorite);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
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
