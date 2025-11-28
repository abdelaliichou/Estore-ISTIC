package com.example.e_commerce.Controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Modele.ItemsModel;
import com.example.e_commerce.Modele.Utils;
import com.example.e_commerce.R;
import com.example.e_commerce.View.Activities.ItemMoreInfomationsActivity;
import com.example.e_commerce.View.Activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder> {

    public ArrayList<ItemsModel> items_list = new ArrayList<>();
    private Context context;
    int exists = 0;

    public AllItemsAdapter(ArrayList<ItemsModel> items_list, Context context) {
        this.context = context;
        this.items_list = items_list;
    }

    @NonNull
    @Override
    public AllItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.populair_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllItemsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//         setting the hearts initial
        Utils.Setting_initial_liked_button(holder.likedImageFill,holder.likedImageEmpty,items_list.get(position).getImageUrl());
        holder.productName.setText(items_list.get(position).getProductName());
        holder.productPrice.setText("$" + items_list.get(position).getPrice());
        holder.card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.card_pop_up));
        Picasso.get().load(items_list.get(position).getImageUrl()).into(holder.productImage);
        if (Float.parseFloat(items_list.get(position).getPrice()) >= 600f){
            holder.ratting.setVisibility(View.VISIBLE);
            holder.populaire.setVisibility(View.VISIBLE);
        } else {
            holder.ratting.setVisibility(View.VISIBLE);
            holder.populaire.setVisibility(View.GONE);
        }
        holder.moreinformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemMoreInfomationsActivity.class);
                intent.putExtra("name", items_list.get(position).getProductName());
                intent.putExtra("price", items_list.get(position).getPrice());
                intent.putExtra("imageurl", items_list.get(position).getImageUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemMoreInfomationsActivity.class);
                intent.putExtra("name", items_list.get(position).getProductName());
                intent.putExtra("price", items_list.get(position).getPrice());
                intent.putExtra("imageurl", items_list.get(position).getImageUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.likedImageEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.likedImageEmpty.getVisibility() == View.VISIBLE) {
                    ChargingItemInFireBase(holder.likedImageFill, holder.likedImageEmpty, position);
                }
            }
        });

        holder.likedImageFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.likedImageFill.getVisibility() == View.VISIBLE) {
                    DeletingItemFromFireBase(holder.likedImageFill, holder.likedImageEmpty, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, likedImageFill, likedImageEmpty , ratting , populaire;
        TextView productName, productPrice;
        MaterialCardView card;
        RelativeLayout moreinformationButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratting = itemView.findViewById(R.id.ratting);
            populaire = itemView.findViewById(R.id.populaire);
            productImage = itemView.findViewById(R.id.product_image);
            likedImageFill = itemView.findViewById(R.id.product_liked_fill);
            likedImageEmpty = itemView.findViewById(R.id.product_liked_empty);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            card = itemView.findViewById(R.id.card);
            moreinformationButton = itemView.findViewById(R.id.more_informations);
        }
    }

    public void ChargingItemInFireBase(ImageView fill, ImageView empty, int position) {
        // charging this item in freebase
        DatabaseReference Root = FirebaseDatabase.getInstance().getReference();
        Root.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // looping throw the firebase children and incrementing the value of the exist variable every time we found  similar items to the one that we want to add to the favorite list
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.getValue(ItemsModel.class).getImageUrl().equals(items_list.get(position).getImageUrl())) {
                        exists = 1;
                        // break;
                    }
                }
                // seeing if the item that we trying to add is already added or note , if note we add it (by verifying the exists variable )
                if (exists == 0) {
                    // there is no item like this added to the favorite , so we add it
                    Root.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(items_list.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, items_list.get(position).getProductName() + " added to favorite !", Toast.LENGTH_SHORT).show();
                                // setting the heart to full , means that this item is from the favorites now
                                MainActivity.bottomBar.setBadgeAtTabId(R.id.Fav, new AnimatedBottomBar.Badge());
                                empty.setVisibility(View.GONE);
                                fill.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    // means that this element already exists in the firebase favorite list , so we don't add it
                } else {
                    exists = 0;
                    Toast.makeText(context, items_list.get(position).getProductName() + " already exists !", Toast.LENGTH_SHORT).show();
                    // this item is already in the favorite list , so in case the heart wasn't full according to some problem , we make it full to
                    // assure that this element is a favorite item
                    MainActivity.bottomBar.setBadgeAtTabId(R.id.Fav, new AnimatedBottomBar.Badge());
                    empty.setVisibility(View.GONE);
                    fill.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void DeletingItemFromFireBase(ImageView fill, ImageView empty, int position) {

        DatabaseReference Root = FirebaseDatabase.getInstance().getReference();
        Root.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // searching for the items that have the same image url which means its the same object , and we delete them
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.getValue(ItemsModel.class).getImageUrl().equals(items_list.get(position).getImageUrl())) {
                        // means that the position adapter image is the same as the one in the firebase
                        Root.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(item.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context, items_list.get(position).getProductName() + " deleted !", Toast.LENGTH_SHORT).show();
                                //sett the heart to empty , means that we have deleted it to our favorite list
                                MainActivity.bottomBar.setBadgeAtTabId(R.id.Fav, new AnimatedBottomBar.Badge());
                                fill.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                exists = 0;
                            }
                        });
                    }
                }
                //sett the heart to empty , means that we have removed it to from our favorite list , even if we haven't find its similar in the database
                MainActivity.bottomBar.setBadgeAtTabId(R.id.Fav, new AnimatedBottomBar.Badge());
                fill.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
