package estore.istic.fr.Resources;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.e_commerce.View.Fragments.FavoriteFragment;
import com.example.e_commerce.View.Fragments.HomeFragment;
import com.example.e_commerce.View.Fragments.OrdersFragment;
import com.example.e_commerce.View.Activities.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.OrderItem;
import estore.istic.fr.Model.Domain.Product;


public class Utils {

    public static ArrayList<Category> list1, list;
    public static ArrayList<Product> list2, list3, list5, list4;
    public static List<SlideModel> slideModels;
    public static int isHere = 0;
    public static int existss = 0;

    // hide the keyboard when we clicks any where(better user experience )

    public static void SettingKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    // hiding the keyboard when we clicks any where ( better user experience )

    public static void setUpKeybaord(View view, Activity activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.SettingKeyboard(activity);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUpKeybaord(innerView, activity);
            }
        }
    }

    public static ArrayList<Category> CategoriesList() {
        if (list1 == null) {
            list1 = new ArrayList<>();
        }
        list1.clear();
        list1.add(new Category("Energy and endurance", "https://img.freepik.com/free-photo/young-sporty-man-concentrated-training-focused-athlete-looking-river-sunset-sportsman-preparing-running-fitness-healthy-lifestyle_342744-607.jpg?w=740&t=st=1685019006~exp=1685019606~hmac=b2a69c60773324b087e9386e78a998a9ad4db09dc779870620c351d94efab4a3"));
        list1.add(new Category("Herbal food supplements", "https://img.freepik.com/premium-photo/alternative-medicine-herbal-organic-capsule-with-vitamin-e-omega-3-fish-oil-mineral-drug-with-herbs-leaf-natural-supplements-healthy-good-life_39768-3472.jpg?w=740"));
        list1.add(new Category("Multivitamins", "https://img.freepik.com/free-photo/glass-fresh-fruit-juice_144627-17244.jpg?w=740&t=st=1685019337~exp=1685019937~hmac=75aa1ca79f08ccf809c5f3cf8570397bb12c671d2abe89c645a9b47c9f04482a"));
        list1.add(new Category("Nutrition sportive", "https://img.freepik.com/free-photo/sneakers-near-healthy-food_23-2147750792.jpg?w=740&t=st=1685019387~exp=1685019987~hmac=b721d550a5377eb1b731c90503db60bd76fb71176f8ce38b534eac54827b9a95"));
        list1.add(new Category("Sport nutrition", "https://img.freepik.com/free-photo/flat-lay-salad-weights_23-2148262144.jpg?w=740&t=st=1685019447~exp=1685020047~hmac=166b1cf654e134b90653ae064c9b275b2750d35641392682d54e75aade66a596"));
        return list1;
    }

    public static ArrayList<Product> initialPopularItemsList() {
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        list2.clear();
        list2.add(new Product("Optimum Nutrition Gold Standard Whey", "2000", "https://content.optimumnutrition.com/i/on/on-gold-standard-100-whey-protein_Image_01?$TTL_PRODUCT_IMAGES$&layer0=$PDP_004$&fmt=auto&img404=no-product-image&v=1&locale=en-us,en-gb,*", false));
        list2.add(new Product("MuscleBlaze Liquid L-Carnitine, 450 ml, Lemon Lime", "99.89", "https://img5.hkrtcdn.com/23182/prd_2318174-MuscleBlaze-Liquid-LCarnitine-450-ml-Lemon-Lime_o.jpg", false));
        list2.add(new Product("GEL30 Nitro Dual-Carb Energy Gel", "160.89", "https://cdn.shopify.com/s/files/1/0551/0388/1250/files/Gel30NitroWeb_700x.jpg?v=1685022967", false));
        list2.add(new Product("Nuun Sport Electrolyte Drink Tablets", "220.69", "https://cdn.shopify.com/s/files/1/0014/3563/1652/products/Tube-Single-w-tabs-Sport-StrawberryLemonade_x900.png?v=1648758046", false));
        list2.add(new Product("Bloom Nutrition Super Greens Powder", "97.94", "https://cdn.shopify.com/s/files/1/0143/0952/3556/products/Greens30_Berry_A.jpg?v=1680798061", false));
        list2.add(new Product("Garden of Life Vitamin Code", "2199.89", "https://www.gardenoflife.com/media/wysiwyg/vc-supplements-for-everybody-1060x1060_1_.jpg", false));
        list2.add(new Product("Optimum Nutrition Micronized Creatine Monohydrate Powder", "1700", "https://content.optimumnutrition.com/i/on/on-creatine-micronized_Image_01?$TTL_PRODUCT_IMAGES$&layer0=$PDP_004$&fmt=auto&img404=no-product-image&v=1&locale=en-us,en-gb,*", false));
        return list2;
    }


    public static List<SlideModel> GetSlideList() {
        if (slideModels == null) {
            slideModels = new ArrayList<>();
        }
        slideModels.clear();
        slideModels.add(new SlideModel("https://img.freepik.com/free-photo/flat-lay-salad-weights_23-2148262144.jpg?w=740&t=st=1685019447~exp=1685020047~hmac=166b1cf654e134b90653ae064c9b275b2750d35641392682d54e75aade66a596", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1593095948071-474c5cc2989d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://plus.unsplash.com/premium_photo-1672352722063-678ed538f80e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1609150990057-f13c984a12f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1587854692152-cbe660dbde88?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=869&q=80", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://plus.unsplash.com/premium_photo-1672759453651-c6834f55c4f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1095&q=80", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1559087316-6b27308e53f6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=794&q=80", ScaleTypes.CENTER_CROP));

        return slideModels;
    }

    public static ArrayList<Product> getPopularItemsListFromDataBase(String Category) {
        DatabaseReference Rot = FirebaseDatabase.getInstance().getReference();
        if (list3 == null) {
            list3 = new ArrayList<>();
        }
        list3.clear();
        Rot.child("Users").child("Products").child("items").child(Category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot category : snapshot.getChildren()) {
                    if (SearchActivity.progressBar != null){
                        SearchActivity.progressBar.setVisibility(View.GONE);
                    }
                    HomeFragment.progressBar1.setVisibility(View.GONE);
                    String Productname = category.getValue(Product.class).getName();
                    String ProductPris = category.getValue(Product.class).getPrice();
                    String ImageUrl = category.getValue(Product.class).getImageUrl();
                    Product model = new Product(Productname, ProductPris, ImageUrl);
                    list3.add(model);
                    if (HomeFragment.adapter2 != null){
                        HomeFragment.adapter2.notifyDataSetChanged();
                    }
                    if (SearchActivity.adapter != null){
                        SearchActivity.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SearchActivity.progressBar.setVisibility(View.GONE);
            }
        });
        return list3;
    }


    public static ArrayList<Category> getCategoriesListFromDataBase() {
        DatabaseReference Rott = FirebaseDatabase.getInstance().getReference();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        Rott.child("Users").child("Products").child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot category : snapshot.getChildren()) {
                    Category model = category.getValue(Category.class);
                    list.add(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return list;
    }

    // getting all the items from all the categories in the fire base
    public static ArrayList<Product> AllItemsList() {
        DatabaseReference Rott = FirebaseDatabase.getInstance().getReference();
        if (list4 == null) {
            list4 = new ArrayList<>();
        }
        list4.clear();
        Rott.child("Users").child("Products").child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HomeFragment.progressBar2.setVisibility(View.GONE);
                for (DataSnapshot category : snapshot.getChildren()) {
                    for (DataSnapshot item : category.getChildren()) {
                        Product model = item.getValue(Product.class);
                        list4.add(model);
                        HomeFragment.adapter3.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                HomeFragment.progressBar2.setVisibility(View.GONE);
            }
        });
        return list4;
    }

    //getting all the favorite items
    public static ArrayList<Product> getFavoriteItems() {

        DatabaseReference Rott = FirebaseDatabase.getInstance().getReference();
        if (list5 == null) {
            list5 = new ArrayList<>();
        }
        list5.clear();
        Rott.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot category : snapshot.getChildren()) {
                    FavoriteFragment.progressBar.setVisibility(View.GONE);
                    Product fav = category.getValue(Product.class);
                    boolean isDouble = false;
                    for (Product i : list5) {
                        if (i != null) {
                            if (i.getImageUrl() != null) {
                                if (i.getImageUrl().equals(fav.getImageUrl())) {
                                    isDouble = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!isDouble) {
                        list5.add(0, fav);
                        FavoriteFragment.adapter.notifyItemInserted(0);
                    }
                    FavoriteFragment.adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                FavoriteFragment.progressBar.setVisibility(View.GONE);
            }
        });
        return list5;
    }

    static public ArrayList<OrderItem> getCardItemsList() {
        ArrayList<OrderItem> liss = new ArrayList<>();
        DatabaseReference head = FirebaseDatabase.getInstance().getReference();
        head.child("UserCard").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    OrderItem model = item.getValue(OrderItem.class);
                    liss.add(0, model);
                    OrdersFragment.adapter.notifyItemInserted(0);
                    OrdersFragment.adapter.notifyDataSetChanged();
                    OrdersFragment.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                OrdersFragment.progressBar.setVisibility(View.GONE);
            }
        });
        return liss;
    }

    public static void Setting_initial_liked_button(ImageView fill, ImageView empty, String uri) {
        DatabaseReference Root = FirebaseDatabase.getInstance().getReference();
        Root.child("FavoriteItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // looping throw the firebase children and incrementing the value of the exist variable every time we found  similar items to the one that we want to add to the favorite list
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.getValue(Product.class).getImageUrl() != null){
                        if (i.getValue(Product.class).getImageUrl().equals(uri)) {
                            existss = 1;
                            break;
                        }
                    }
                }
                // seeing if this product is in the favorites
                if (existss == 0) {
                    // this item is note favorite ,so we show to empty heart
                    fill.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else { // this item is in the favorite ,so we show to fill heart
                    existss = 0;
                    fill.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}


