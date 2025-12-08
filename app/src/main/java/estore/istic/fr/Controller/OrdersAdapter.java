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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import estore.istic.fr.Facade.OnOrderActionListener;
import estore.istic.fr.Model.Domain.CartItem;
import estore.istic.fr.Model.Domain.Order;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Utils;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    List<Order> OrdersList;
    Context context;
    OnOrderActionListener listener;

    public OrdersAdapter(
            List<Order> OrdersList,
            OnOrderActionListener listener,
            Context context
    ) {
        this.context = context;
        this.listener = listener;
        this.OrdersList = OrdersList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item2, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = OrdersList.get(position);
        holder.date.setText(Utils.parseDate(order.getOrderDate()));
        holder.totalPrice.setText(String.valueOf(order.getTotalPrice()));
        holder.orderId.setText("Order #".concat(order.getOrderId().substring(0,7)));
        holder.cardParent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.card_pop_up));
        holder.quantity.setText(String.valueOf(
                order.getItems()
                        .stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum()
        ));

        listener.onOrderStatus(
                holder.statusParent,
                holder.statusText,
                order
        );

        holder.cardParent.setOnClickListener(v -> {
            listener.onOrderClicked(order);
        });

        // thumbnails logic
        List<String> productImages = order.getItems()
                .stream()
                .map(item -> item.getProduct().getImageUrl())
                .collect(Collectors.toList());

        int totalItems = productImages.size();

        if (totalItems >= 1) {
            holder.card1.setVisibility(View.VISIBLE);
            Picasso.get().load(productImages.get(0)).into(holder.img1);
        } else {
            holder.card1.setVisibility(View.GONE);
        }

        if (totalItems >= 2) {
            holder.card2.setVisibility(View.VISIBLE);
            Picasso.get().load(productImages.get(1)).into(holder.img2);
        } else {
            holder.card2.setVisibility(View.GONE);
        }

        if (totalItems >= 3) {
            holder.card3.setVisibility(View.VISIBLE);
            Picasso.get().load(productImages.get(2)).into(holder.img3);
        } else {
            holder.card3.setVisibility(View.GONE);
        }

        if (totalItems >= 4) {
            holder.frameMore.setVisibility(View.VISIBLE);
            Picasso.get().load(productImages.get(3)).into(holder.imgMore);
            if (totalItems > 4) {
                holder.moreCount.setVisibility(View.VISIBLE);
                holder.moreCard.setVisibility(View.VISIBLE);
                holder.moreCount.setText("+".concat(String.valueOf(totalItems - 3)));
            } else {
                holder.moreCount.setVisibility(View.GONE);
                holder.moreCard.setVisibility(View.GONE);
            }
        } else {
            holder.frameMore.setVisibility(View.GONE);
        }

    }

    private void orderProductsThumbnails() {

    }

    @Override
    public int getItemCount() {
        return OrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice, date, statusText, orderId, moreCount, quantity;
        RelativeLayout statusParent, frameMore;
        MaterialCardView cardParent ;
        CardView card1, card2, card3, moreCard;
        ImageView img1, img2, img3, imgMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            orderId = itemView.findViewById(R.id.orderId);
            cardParent = itemView.findViewById(R.id.card_item);
            totalPrice = itemView.findViewById(R.id.orderPrice);
            date = itemView.findViewById(R.id.orderDate);
            statusText = itemView.findViewById(R.id.orderStatus);
            statusParent = itemView.findViewById(R.id.orderStatusParent);

            card1 = itemView.findViewById(R.id.card_thumb_1);
            card2 = itemView.findViewById(R.id.card_thumb_2);
            card3 = itemView.findViewById(R.id.card_thumb_3);
            moreCard = itemView.findViewById(R.id.moreCard);
            frameMore = itemView.findViewById(R.id.frame_thumb_more);

            moreCount = itemView.findViewById(R.id.moreCount);

            img1 = itemView.findViewById(R.id.img_thumb_1);
            img2 = itemView.findViewById(R.id.img_thumb_2);
            img3 = itemView.findViewById(R.id.img_thumb_3);
            imgMore = itemView.findViewById(R.id.img_thumb_more);
        }
    }
}
