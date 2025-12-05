package estore.istic.fr.View.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;
import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.databaseHelper;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;
import estore.istic.fr.View.loginActivity;

public class optionsFragment extends Fragment {

    MaterialCardView deliveryProcessButton, ordersHistoryButton, updateButton, contactLayout, logoutButton;
    TextView name, email;

    AlertDialog dialog;
    private Optional<Context> safeContext;

    public optionsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        safeContext = Optional.of(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        safeContext = Optional.empty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_options, container, false);

        Utils.statusAndActionBarIconsColor(getActivity(), R.id.main);

        initialisation(view);
        setAnimations(view);
        loadUserData();

        handlingOnClicks();

        return view;
    }

    public void initialisation(View view) {
        deliveryProcessButton = view.findViewById(R.id.localisation);
        ordersHistoryButton = view.findViewById(R.id.emaillayout);
        updateButton = view.findViewById(R.id.phone);
        logoutButton = view.findViewById(R.id.logout);
        contactLayout = view.findViewById(R.id.contact);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
    }

    public void setAnimations(View view) {
        Animations.FromeLeftToRight(view.findViewById(R.id.info));
        Animations.FromeRightToLeftCardd(view.findViewById(R.id.parent));
        Animations.FromeRightToLeftCard1(view.findViewById(R.id.localisation));
        Animations.FromeRightToLeftCard2(view.findViewById(R.id.emaillayout));
        Animations.FromeRightToLeftCard3(view.findViewById(R.id.phone));
        Animations.FromeRightToLeftCard4(view.findViewById(R.id.contact));
        Animations.FromeRightToLeftCard5(view.findViewById(R.id.logout));
    }

    public void loadUserData() {
        UsersService.getUserName(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                name.setText(userName);
                email.setText(userEmail);
            }

            @Override
            public void onError(String message) {
                safeContext.ifPresent(context -> {
                    Utils.showToast(context, message);
                });
            }
        });
    }

    public void handlingOnClicks() {
        //updateButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), Update_Activity.class)));
        //ordersHistoryButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), AllOrders_Activity.class)));
        //contactLayout.setOnClickListener(view -> startActivity(new Intent(getActivity(), ContactUs_Activity.class)));
        logoutButton.setOnClickListener(view -> {
            safeContext.ifPresent(context -> {
                dialog = Utils.createDialog(
                        safeContext.get(),
                        "Déconnecter ?",
                        "Vous êtes sûr que vous voulez déconnecter ?",
                        true,
                        R.drawable.ic_logout,
                        R.drawable.alert_dialog_back,
                        true,
                        () -> {
                            databaseHelper.getAuth().signOut();
                            requireActivity().finishAffinity();
                            startActivity(new Intent(view.getContext(), loginActivity.class));
                        },
                        () -> {}
                );
                dialog.show();
            });
        });

        //deliveryProcessButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), OrderDetails_Activity.class).putExtra("id","last_order")));

    }

}