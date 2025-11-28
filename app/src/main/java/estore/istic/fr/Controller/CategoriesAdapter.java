package com.example.e_commerce.Controler;

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

import com.example.e_commerce.Modele.CategoryModel;
import com.example.e_commerce.Modele.Utils;
import com.example.e_commerce.R;
import com.example.e_commerce.View.Fragments.HomeFragment;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class categories_adapter extends RecyclerView.Adapter<categories_adapter.ViewHolder> {
    int isclicked = -1;
    private ArrayList<CategoryModel> Category_list = new ArrayList<>();
    private Context context;

    public categories_adapter(ArrayList<CategoryModel> tasks_list, Context context) {
        this.context = context;
        this.Category_list = tasks_list;
    }

    @NonNull
    @Override
    public categories_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull categories_adapter.ViewHolder holder, int position) {
        holder.text.setText(Category_list.get(position).getCategory());
        Picasso.get().load(Category_list.get(position).getImageUrl()).into(holder.image);
//        Animations.FromeRightToLeftCard(holder.cardView);
        if (position == isclicked) { // the clicked item
            holder.cardView.setStrokeWidth(6);
            holder.cardView.setStrokeColor(R.color.colorfirst);
            holder.backEffect.setBackground(null);
            holder.text.setTextColor(context.getResources().getColor(R.color.colorfirst));
            //changing the popular items list to the items that correspond with the category item clicked
            HomeFragment.progressBar1.setVisibility(View.VISIBLE);
            HomeFragment.adapter2.items_list = (Utils.getPopularItemsListFromDataBase(Category_list.get(position).getCategory())) ;
            //HomeFragment.adapter2.notifyDataSetChanged();

        } else { // all the not clicked items
            holder.cardView.setStrokeWidth(0);
            holder.backEffect.setBackgroundColor(context.getResources().getColor(R.color.beckeffect));
            holder.text.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return Category_list.size();
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
