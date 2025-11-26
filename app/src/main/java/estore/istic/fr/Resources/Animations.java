package estore.istic.fr.Resources;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

public class Animations {
    static int duree = 450 ;
    static int DURATION = 850;
    static int DURATION1 = 750;
    static int DURATION2 = 350;
    static int DURATION3 = 450 ;
    static int DURATION4 = 550 ;
    static int DURATION5 = 650 ;
    static int DURATION6 = 750 ;
    static boolean ON_ATTACH = true;

    public static void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }


    public static void Fromdowntoup(MaterialCardView itemView, int i) {
        if (!ON_ATTACH) {
            i = -1;
        }
        boolean not_first_item = i == -1;
        i = i + 1;
        itemView.setTranslationZ(itemView.getZ() + 200);
        //itemView.setTranslationY(itemView.getY() + 200);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationZ", itemView.getZ() + 200, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * duree);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeLeftToRightlate(MaterialCardView itemView) {

        itemView.setTranslationX(itemView.getX() - 1500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animatorTranslateX.setStartDelay(2000);
        animatorTranslateX.setDuration(DURATION6+250);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeLeftToRight3(TextView itemView) {

        itemView.setTranslationX(itemView.getX() - 900);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeLeftToRightlateImage(ImageView itemView) {

        itemView.setTranslationX(itemView.getX() - 1500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animatorTranslateX.setStartDelay(3500);
        animatorTranslateX.setDuration(DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +900);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION4);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCardd(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * 250);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard1(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard2(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard3(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION4);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard4(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION5);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftCard5(MaterialCardView itemView) {
        itemView.setTranslationX(itemView.getX() +500);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 900, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeft(RelativeLayout itemView) {

        itemView.setTranslationX(itemView.getX() + 300);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromLeftToRightCardView(MaterialCardView itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
//          animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION));
        animatorTranslateX.setDuration((true ? 2 : 1) * (DURATION-400));
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }


    public static void FromeLeftToRight(TextView itemView) {

        itemView.setTranslationX(itemView.getX() - 900);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeLeftToRight1(TextView itemView) {

        itemView.setTranslationX(itemView.getX() - 900);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromRightToLeft1(TextView itemView) {

        itemView.setTranslationX(itemView.getX() + 300);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }


    public static void FromeDownToUp(LinearLayout itemView) {

        itemView.setTranslationY(itemView.getY() + 300);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationY", itemView.getY() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        // animatorTranslateX.setStartDelay(not_first_item ? DURATION1 : (i * DURATION1));
        animatorTranslateX.setDuration((true ? 2 : 1) * DURATION1);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromUpToDown(ImageView itemView) {

        itemView.setTranslationY(itemView.getY() - 710);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationY", itemView.getY() - 700, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((true ? 2 : 1) * DURATION);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromUpToDownSignup(ImageView itemView) {

        itemView.setTranslationY(itemView.getY() - 710);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationY", itemView.getY() - 700, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", .17f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //animatorTranslateX.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateX.setDuration((true ? 2 : 1) * DURATION);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }


    public static void FromeRightToLeftPinview(PinView itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
  }

    public static void FromeLeftToRightLinear(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeLeftToRightLinear1(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeLeftToRightLinear2(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION4);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeLeftToRightLinear3(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION5);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeLeftToRightLinear4(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftEditetext(TextInputLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftEditetext1(TextInputLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftEditetext2(TextInputLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION4);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftEditetext3(TextInputLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION5);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftEditetext4(TextInputLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION6);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }

    public static void FromeRightToLeftLinear(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION2);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftLinear1(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION3);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
    public static void FromeRightToLeftLinear2(LinearLayout itemView) {

        itemView.setTranslationX(itemView.getX() - 800);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 300, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        //  animatorTranslateX.setStartDelay(not_first_item ? DURATION2 : (i * DURATION2));
        animatorTranslateX.setDuration((/*not_first_item*/true ? 2 : 1) * DURATION4);
        animatorSet.playTogether(animatorTranslateX, animatorAlpha);
        animatorSet.start();
    }
}
