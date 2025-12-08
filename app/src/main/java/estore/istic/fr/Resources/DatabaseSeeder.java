package estore.istic.fr.Resources;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import estore.istic.fr.Model.Domain.Category;
import estore.istic.fr.Model.Domain.Product;
import estore.istic.fr.Model.Dto.ProductDto;

public class DatabaseSeeder {
    public static void seed() {
        DatabaseReference catRef = DatabaseHelper.getDatabaseReference().child("categories");
        DatabaseReference prodRef = DatabaseHelper.getDatabaseReference().child("products");

        ArrayList<Category> categories = categoriesList();
        ArrayList<ProductDto> products = productsList();

        List<String> uploadedCategoryIds = new ArrayList<>();

        // ==========================================
        // STEP 1: UPLOAD CATEGORIES
        // ==========================================
        for (Category cat : categories) {
            String key = catRef.push().getKey();

            cat.setCategoryId(key);

            if (key != null) {
                catRef.child(key).setValue(cat);
                uploadedCategoryIds.add(key);
            }
        }

        Log.d("SEEDER", "Uploaded " + categories.size() + " categories.");

        // ==========================================
        // STEP 2: UPLOAD PRODUCTS
        // ==========================================

        // assign products to random categories
        Random random = new Random();

        for (ProductDto dto : products) {

            Product product = dto.getProduct();

            String key = prodRef.push().getKey();

            product.setProductId(key);

            // pick a random ID from the categories we just uploaded
            if (!uploadedCategoryIds.isEmpty()) {
                String randomCatId = uploadedCategoryIds.get(random.nextInt(uploadedCategoryIds.size()));
                product.setCategoryId(randomCatId);
            }

            // Save to "products" node
            if (key != null) {
                prodRef.child(key).setValue(product)
                        .addOnSuccessListener(unused -> Log.d("SEEDER", "Product Saved: " + product.getName()))
                        .addOnFailureListener(e -> Log.e("SEEDER", "Failed: " + e.getMessage()));
            }
        }
    }

    public static ArrayList<Category> categoriesList() {
        ArrayList<Category> list = new ArrayList<>();

        // --- Original Categories ---
        list.add(new Category("Energy and endurance", "https://img.freepik.com/free-photo/young-sporty-man-concentrated-training-focused-athlete-looking-river-sunset-sportsman-preparing-running-fitness-healthy-lifestyle_342744-607.jpg?w=740&t=st=1685019006~exp=1685019606~hmac=b2a69c60773324b087e9386e78a998a9ad4db09dc779870620c351d94efab4a3"));
        list.add(new Category("Herbal food supplements", "https://img.freepik.com/premium-photo/alternative-medicine-herbal-organic-capsule-with-vitamin-e-omega-3-fish-oil-mineral-drug-with-herbs-leaf-natural-supplements-healthy-good-life_39768-3472.jpg?w=740"));
        list.add(new Category("Multivitamins", "https://img.freepik.com/free-photo/glass-fresh-fruit-juice_144627-17244.jpg?w=740&t=st=1685019337~exp=1685019937~hmac=75aa1ca79f08ccf809c5f3cf8570397bb12c671d2abe89c645a9b47c9f04482a"));
        list.add(new Category("Nutrition sportive", "https://img.freepik.com/free-photo/sneakers-near-healthy-food_23-2147750792.jpg?w=740&t=st=1685019387~exp=1685019987~hmac=b721d550a5377eb1b731c90503db60bd76fb71176f8ce38b534eac54827b9a95"));
        list.add(new Category("Sport nutrition", "https://img.freepik.com/free-photo/flat-lay-salad-weights_23-2148262144.jpg?w=740&t=st=1685019447~exp=1685020047~hmac=166b1cf654e134b90653ae064c9b275b2750d35641392682d54e75aade66a596"));

        // --- New Categories (Matching the new Products) ---

        // 1. For Whey, Mass Gainers, Casein
        list.add(new Category("Protein & Muscle Building", "https://images.unsplash.com/photo-1579722821273-0f6c7d44362f?auto=format&fit=crop&w=800&q=80"));

        // 2. For Pre-workouts (C4, Curse) & Amino Acids
        list.add(new Category("Pre-Workout & Performance", "https://images.unsplash.com/photo-1623874514711-0f321325f318?auto=format&fit=crop&w=800&q=80"));

        // 3. For Fat Burners (L-Carnitine, Ripped) & Diet
        list.add(new Category("Weight Loss & Management", "https://images.unsplash.com/photo-1511690656952-34342d5c2895?auto=format&fit=crop&w=800&q=80"));

        // 4. For Protein Bars, Cookies, Snacks
        list.add(new Category("Healthy Snacks & Bars", "https://images.unsplash.com/photo-1622483767128-3f66f32aef97?auto=format&fit=crop&w=800&q=80"));

        // 5. For Shakers, Belts, Gym Gear
        list.add(new Category("Gym Accessories & Gear", "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?auto=format&fit=crop&w=800&q=80"));

        return list;
    }

    public static ArrayList<ProductDto> productsList() {
        ArrayList<ProductDto> list = new ArrayList<>();

        // --- Original Items (Preserved) ---
        list.add(new ProductDto(new Product("Optimum Nutrition Gold Standard Whey", 2000, "https://content.optimumnutrition.com/i/on/on-gold-standard-100-whey-protein_Image_01?$TTL_PRODUCT_IMAGES$&layer0=$PDP_004$&fmt=auto&img404=no-product-image&v=1&locale=en-us,en-gb,*"), false));
        list.add(new ProductDto(new Product("MuscleBlaze Liquid L-Carnitine, 450 ml, Lemon Lime", 99.89, "https://img5.hkrtcdn.com/23182/prd_2318174-MuscleBlaze-Liquid-LCarnitine-450-ml-Lemon-Lime_o.jpg"), false));
        list.add(new ProductDto(new Product("GEL30 Nitro Dual-Carb Energy Gel", 160.89, "https://cdn.shopify.com/s/files/1/0551/0388/1250/files/Gel30NitroWeb_700x.jpg?v=1685022967"), false));
        list.add(new ProductDto(new Product("Nuun Sport Electrolyte Drink Tablets", 220.69, "https://cdn.shopify.com/s/files/1/0014/3563/1652/products/Tube-Single-w-tabs-Sport-StrawberryLemonade_x900.png?v=1648758046"),false));
        list.add(new ProductDto(new Product("Bloom Nutrition Super Greens Powder", 97.94, "https://cdn.shopify.com/s/files/1/0143/0952/3556/products/Greens30_Berry_A.jpg?v=1680798061"), false));
        list.add(new ProductDto(new Product("Garden of Life Vitamin Code", 2199.89, "https://www.gardenoflife.com/media/wysiwyg/vc-supplements-for-everybody-1060x1060_1_.jpg"), false));
        list.add(new ProductDto(new Product("Optimum Nutrition Micronized Creatine Monohydrate Powder", 1700, "https://content.optimumnutrition.com/i/on/on-creatine-micronized_Image_01?$TTL_PRODUCT_IMAGES$&layer0=$PDP_004$&fmt=auto&img404=no-product-image&v=1&locale=en-us,en-gb,*"), false));

        // --- New Added Popular Products (with Valid Unsplash Images) ---

        // Proteins
        list.add(new ProductDto(new Product("BSN Syntha-6 Edge Protein Powder", 1850.50, "https://images.unsplash.com/photo-1579722821273-0f6c7d44362f?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("MuscleTech Nitro-Tech Ripped", 2100.00, "https://images.unsplash.com/photo-1579722820308-d74e571900a9?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Dymatize ISO 100 Hydrolyzed", 2300.75, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("MusclePharm Combat Protein Powder", 1950.00, "https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?auto=format&fit=crop&w=800&q=80"), false));

        // Pre-Workouts & Energy
        list.add(new ProductDto(new Product("Cellucor C4 Original Pre-Workout", 1250.00, "https://images.unsplash.com/photo-1623874514711-0f321325f318?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("XTEND Original BCAA Powder", 1100.25, "https://images.unsplash.com/photo-1606902965551-dce0617340ad?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Gaspari Nutrition SuperPump Max", 1350.50, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Cobra Labs The Curse Pre-Workout", 1200.00, "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?auto=format&fit=crop&w=800&q=80"), false));

        // Vitamins & Health
        list.add(new ProductDto(new Product("Universal Nutrition Animal Pak", 1500.00, "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Now Foods Omega-3 Fish Oil", 850.75, "https://images.unsplash.com/photo-1550572017-edd951aa8f72?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Nordic Naturals Ultimate Omega", 1150.00, "https://images.unsplash.com/photo-1616671268670-13c5cf707ce5?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Nature's Way Alive! Multivitamin", 950.00, "https://images.unsplash.com/photo-1585435557343-3b092031a831?auto=format&fit=crop&w=800&q=80"), false));

        // Bars & Snacks
        list.add(new ProductDto(new Product("Quest Nutrition Protein Bar (Box of 12)", 2400.00, "https://images.unsplash.com/photo-1622483767128-3f66f32aef97?auto=format&fit=crop&w=800&q=80"), false)); // Placeholder bar image
        list.add(new ProductDto(new Product("Grenade Carb Killa High Protein Bar", 180.50, "https://images.unsplash.com/photo-1622483767128-3f66f32aef97?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("RXBAR Whole Food Protein Bar", 150.25, "https://images.unsplash.com/photo-1600093463592-8e36ae95ef56?auto=format&fit=crop&w=800&q=80"), false));

        // Accessories
        list.add(new ProductDto(new Product("BlenderBottle Classic V2 Shaker", 450.00, "https://images.unsplash.com/photo-1577174881658-0f30ed549adc?auto=format&fit=crop&w=800&q=80"), false));
        list.add(new ProductDto(new Product("Gym Shark Lifting Belt", 1200.00, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?auto=format&fit=crop&w=800&q=80"), false));

        return list;
    }
}
