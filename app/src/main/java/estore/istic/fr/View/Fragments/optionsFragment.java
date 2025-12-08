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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Optional;

import estore.istic.fr.Facade.OnUserActionListener;
import estore.istic.fr.R;
import estore.istic.fr.Resources.Animations;
import estore.istic.fr.Resources.DatabaseHelper;
import estore.istic.fr.Resources.Utils;
import estore.istic.fr.Services.UsersService;
import estore.istic.fr.View.contactUsActivity;
import estore.istic.fr.View.loginActivity;
import estore.istic.fr.View.orderDetailsActivity;
import estore.istic.fr.View.ordersActivity;
import estore.istic.fr.View.userProfileActivity;

public class optionsFragment extends Fragment {

    RelativeLayout deliveryProcessButton, ordersHistoryButton, updateButton, contactLayout, logoutButton;
    TextView name, number;

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
        deliveryProcessButton = view.findViewById(R.id.lastOrder);
        ordersHistoryButton = view.findViewById(R.id.history);
        updateButton = view.findViewById(R.id.update);
        logoutButton = view.findViewById(R.id.logout);
        contactLayout = view.findViewById(R.id.contact);
        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);
    }

    public void setAnimations(View view) {
        Animations.FromeLeftToRight(view.findViewById(R.id.info));
        Animations.FromeRightToLeftCardd(view.findViewById(R.id.parent));
        Animations.FromeRightToLeftCard1(view.findViewById(R.id.lastOrder));
        Animations.FromeRightToLeftCard2(view.findViewById(R.id.history));
        Animations.FromeRightToLeftCard3(view.findViewById(R.id.update));
        Animations.FromeRightToLeftCard4(view.findViewById(R.id.contact));
        Animations.FromeRightToLeftCard5(view.findViewById(R.id.logout));
    }

    public void loadUserData() {
        UsersService.getUserData(new OnUserActionListener() {
            @Override
            public void onSuccess(String userName, String userEmail, String phoneNumber) {
                name.setText(userName);
                number.setText(phoneNumber);
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
        deliveryProcessButton.setOnClickListener(view -> {
            // we want the last order done by this user
            startActivity(new Intent(
                            getActivity(),
                            orderDetailsActivity.class
                    ).putExtra("isLast", true)
            );
        });
        ordersHistoryButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), ordersActivity.class)));
        updateButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), userProfileActivity.class)));
        contactLayout.setOnClickListener(view -> startActivity(new Intent(getActivity(), contactUsActivity.class)));
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
                            DatabaseHelper.getAuth().signOut();
                            requireActivity().finishAffinity();
                            startActivity(new Intent(view.getContext(), loginActivity.class));
                        },
                        () -> {
                        }
                );
                dialog.show();
            });
        });
    }

}