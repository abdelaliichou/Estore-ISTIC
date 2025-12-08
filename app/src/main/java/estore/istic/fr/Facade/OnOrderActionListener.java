package estore.istic.fr.Facade;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import estore.istic.fr.Model.Domain.Order;

public interface OnOrderActionListener {
    void onOrderClicked(Order order);
    void onOrderStatus(
            RelativeLayout parent,
            TextView status,
            Order order
    );
}
