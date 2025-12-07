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

import estore.istic.fr.Facade.OnOrderActionListener;
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
        Picasso.get().load(Utils.orderImageUrl).into(holder.orderIMG);
        holder.date.setText(Utils.parseDate(order.getOrderDate()));
        holder.totalPrice.setText(String.valueOf(order.getTotalPrice()));
        holder.cardParent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.card_pop_up));

        holder.cardParent.setOnClickListener(v -> {
            listener.onOrderClicked(order);
        });

        holder.MoreInfoButton.setOnClickListener(v -> {
            listener.onOrderClicked(order);
        });

    }

    @Override
    public int getItemCount() {
        return OrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice, date ;
        MaterialCardView MoreInfoButton, cardParent ;
        ImageView orderIMG;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIMG = itemView.findViewById(R.id.orderIMG);
            cardParent = itemView.findViewById(R.id.card_item);
            totalPrice = itemView.findViewById(R.id.orderPrice);
            date = itemView.findViewById(R.id.orderDate);
            MoreInfoButton = itemView.findViewById(R.id.more);
        }
    }
}
