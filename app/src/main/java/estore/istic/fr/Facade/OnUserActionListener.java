package estore.istic.fr.Facade;

import java.util.Optional;

public interface OnUserActionListener {
     void onSuccess(
             String userName,
             String userEmail,
             String phoneNumber
     );
     void onError(String message);
}
