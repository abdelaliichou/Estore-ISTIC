package estore.istic.fr.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import estore.istic.fr.Facade.OnCategoryActionListener;
import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.R;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private static final int VIEW_TYPE_LIST = 0;
    private static final int VIEW_TYPE_GRID = 1;
    int isClicked = -1;
    List<Category> categoriesList;
    OnCategoryActionListener listener;
    Context context;
    Boolean isGrid;

    public CategoriesAdapter(
            Context context,
            OnCategoryActionListener listener,
            List<Category> tasks_list,
            Boolean isGrid
    ) {
        this.isGrid = isGrid;
        this.context = context;
        this.listener = listener;
        this.categoriesList = tasks_list;
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
            layout = R.layout.all_categories_layoutitem;
        } else {
            layout = R.layout.category_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(
                layout,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category category = categoriesList.get(position);
        Picasso.get().load(category.getImageUrl()).into(holder.image);
        holder.text.setText(category.getName());

        if (!isGrid) {
            if (position == isClicked) {
                holder.cardView.setStrokeWidth(6);
                holder.backEffect.setBackground(null);
                holder.cardView.setStrokeColor(R.color.colorfirst);
                holder.text.setTextColor(context.getResources().getColor(R.color.colorfirst));
            } else {
                holder.cardView.setStrokeWidth(0);
                holder.backEffect.setBackgroundColor(context.getResources().getColor(R.color.beckeffect));
                holder.text.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        holder.cardView.setOnClickListener(v -> {
            isClicked = position;
            notifyDataSetChanged();
            listener.onCategoryClicked(category);
        });

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        MaterialCardView cardView;
        RelativeLayout backEffect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            text = itemView.findViewById(R.id.textName);
            cardView = itemView.findViewById(R.id.card_parent);
            backEffect = itemView.findViewById(R.id.backeffect);
        }
    }
}
