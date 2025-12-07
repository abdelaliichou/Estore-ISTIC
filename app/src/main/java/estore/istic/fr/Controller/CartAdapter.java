package estore.istic.fr.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import estore.istic.fr.Facade.OnCartAdapterListener;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final int VIEW_CART = 0;
    private static final int VIEW_RECEIPT = 1;

    OnCartAdapterListener cartListener;

    List<CartItem> cartItems;
    Context context;
    boolean isCart;

    public CartAdapter(
            List<CartItem> list,
            Context context ,
            boolean isCart,
            OnCartAdapterListener listener
    ) {
        this.isCart = isCart;
        this.cartItems = list;
        this.context = context;
        this.cartListener = listener ;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public int getItemViewType(int position) {
        return isCart ? VIEW_CART : VIEW_RECEIPT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;
        if (viewType == VIEW_CART) {
            layout = R.layout.card_item;
        } else {
            layout = R.layout.card_item_receipt;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(
                layout,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CartItem cartItem = cartItems.get(position);
        Picasso.get().load(cartItems.get(position).getProduct().getImageUrl())
                .into(holder.image);

        holder.nameText.setText(cartItem.getProduct().getName().concat(".."));
        holder.priceText.setText(String.valueOf(cartItem.getProduct().getPrice()));
        holder.totalItemsText.setText("Quantity: ".concat(String.valueOf(cartItem.getQuantity())));

        holder.card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.card_pop_up));

        if (isCart){
            if (cartItem.getProduct().getPrice() >= 600f){
                holder.hot.setVisibility(View.VISIBLE);
            } else {
                holder.hot.setVisibility(View.GONE);
            }
        }

        holder.card.setOnLongClickListener(view -> {
            cartListener.onProductLongClicked(cartItem);
            return false;
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<CartItem> newList) {
        this.cartItems.clear();
        this.cartItems = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image , ratting , hot;
        TextView totalItemsText, priceText, nameText;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratting = itemView.findViewById(R.id.ratting);
            hot = itemView.findViewById(R.id.populaire);
            card = itemView.findViewById(R.id.card_item);
            image = itemView.findViewById(R.id.item_image);
            totalItemsText = itemView.findViewById(R.id.total_items);
            priceText = itemView.findViewById(R.id.item_price);
            nameText = itemView.findViewById(R.id.item_name);
        }
    }
}
