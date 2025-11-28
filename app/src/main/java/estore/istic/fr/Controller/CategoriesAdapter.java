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

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.R;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    int isclicked = -1;
    List<Category> categoriesList;
    Context context;

    public CategoriesAdapter(
            Context context,
            List<Category> tasks_list
    ) {
        this.context = context;
        this.categoriesList = tasks_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(categoriesList.get(position).getName());
        Picasso.get().load(categoriesList.get(position).getImageUrl()).into(holder.image);
        if (position == isclicked) { // the clicked item
            holder.cardView.setStrokeWidth(6);
            holder.cardView.setStrokeColor(R.color.colorfirst);
            holder.backEffect.setBackground(null);
            holder.text.setTextColor(context.getResources().getColor(R.color.colorfirst));
            //changing the popular items list to the items that correspond with the category item clicked
            // HomeFragment.progressBar1.setVisibility(View.VISIBLE);
            // HomeFragment.adapter2.items_list = (Utils.getPopularItemsListFromDataBase(Category_list.get(position).getCategory())) ;
            //HomeFragment.adapter2.notifyDataSetChanged();

        } else { // all the not clicked items
            holder.cardView.setStrokeWidth(0);
            holder.backEffect.setBackgroundColor(context.getResources().getColor(R.color.beckeffect));
            holder.text.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isclicked = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
