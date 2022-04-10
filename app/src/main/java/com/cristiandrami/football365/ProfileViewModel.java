package com.cristiandrami.football365;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cristiandrami.football365.model.Utilities;
import com.cristiandrami.football365.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
/**
 * This class is the model object of the ProfileFragment class, in this class we retrieve information
 * about the current user logged from the Firestore Database.
 * We use the information to fill the graphical objects used in the Profile UI fragment
 *
 * @return      LiveData<String> objects used in the ProfileFragment class to set the graphical values dinamically
 * @see         ProfileFragment
 * @author      Cristian D. Dramisino
 *
 */
public class ProfileViewModel extends ViewModel {


    private Map<String, Object> userInfo;
    private User currentUser;

    private final MutableLiveData<String> firstName;
    private final MutableLiveData<String> fullName;
    private final MutableLiveData<String> lastName;
    private final MutableLiveData<String> email;

    public ProfileViewModel() {
        currentUser=new User();
        firstName = new MutableLiveData<>();
        lastName = new MutableLiveData<>();
        fullName = new MutableLiveData<>();

        email= new MutableLiveData<>();

        fetchUserData();

    }

    private void fetchUserData() {
        String currentUserEmail= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Log.e("user auth email", currentUserEmail);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(currentUserEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.e("user", "DocumentSnapshot data: " + document.getData());
                        userInfo= document.getData();

                        String emailObtained= (String) userInfo.get(Utilities.FIREBASE_DOCUMENT_FIELD_EMAIL);
                        String firstNameObtained= (String) userInfo.get(Utilities.FIREBASE_DOCUMENT_FIELD_FIRST_NAME);
                        String lastNameObtained= (String) userInfo.get(Utilities.FIREBASE_DOCUMENT_FIELD_LAST_NAME);
                        // Log.e("user auth email", emailObtained);


                        email.setValue(emailObtained);
                        firstName.setValue(firstNameObtained);
                        lastName.setValue(lastNameObtained);
                        fullName.setValue(firstNameObtained+ " " + lastNameObtained);
                        //Log.e("user", currentUser.getEmail());
                        //Log.e("email", email.getValue().toString());

                    } else {
                        Log.d("user", "No such document");
                    }
                } else {
                    Log.d("user", "get failed with ", task.getException());
                }
            }
        });


    }

    public LiveData<String> getFirstName() {
        return firstName;
    }

    public LiveData<String> getLastName() {
        return lastName;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getFullName() {
        return fullName;
    }

    public void updateInformation(User newInformation) {
        String userEmail=newInformation.getEmail();

        Log.e("update", userEmail);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userEmail);
        Map<String, Object> user= new HashMap<>();
        user.put(Utilities.FIREBASE_DOCUMENT_FIELD_EMAIL, newInformation.getEmail());
        user.put(Utilities.FIREBASE_DOCUMENT_FIELD_FIRST_NAME, newInformation.getFirstName());
        user.put(Utilities.FIREBASE_DOCUMENT_FIELD_LAST_NAME, newInformation.getLastName());
        docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    email.setValue(newInformation.getEmail());
                    firstName.setValue(newInformation.getFirstName());
                    lastName.setValue(newInformation.getLastName());
                    fullName.setValue(newInformation.getFirstName()+ " " + newInformation.getLastName());
                }

            }
        });
    }

    public void updateAuthentication(String oldPassword, String newPassword) {

        //TODO control
       FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
       AuthCredential oldCredential = EmailAuthProvider.getCredential(user.getEmail(),oldPassword);
       user.reauthenticate(oldCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){
                               Log.e("password", "updated "+ newPassword);
                               return;
                           }

                       }
                   });
               }
               else
               {
                   Log.e("update failed", "password not valid");
               }

           }
       });

    }
}