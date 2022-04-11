package com.cristiandrami.football365.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentProfileBinding;
import com.cristiandrami.football365.model.user.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This class is used as a Controller for the Profile Fragment
 * It contains all methods to manage the Profile Fragment Graphic.
 * It contains an object used to manage the Model of Profile Fragment
 *
 * @see ProfileViewModel
 * @author Cristian D. Dramisino
 *
 */

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Button updateButton;
    private Button dialogConfirmationButton;

    private Dialog updatePopup;
    private TextInputEditText firstNameFieldEditTextProfileFragment;
    private TextInputEditText newPasswordFieldEditTextProfileFragment;
    private TextInputEditText oldPasswordFieldEditTextProfileFragment;
    private TextInputLayout oldPasswordFieldLayoutProfileFragment;
    private TextInputEditText lastNameTextInputEditTextProfileFragment;
    private TextView emailTextView;
    private TextView fullNameTextView;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         profileViewModel =
            new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();





        setBindInformation();


        setListenerOnUpdateButton();
        //setListenerOnNewPasswordEditText();

        //makeOldPasswordFieldsInvisible();
        return root;
    }

    /*private void setListenerOnNewPasswordEditText() {
        newPasswordFieldEditTextProfileFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){

                }else{
                    makeOldPasswordFieldsInvisible();

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){
                    makeOldPasswordFieldsVisible();
                }else{
                    makeOldPasswordFieldsInvisible();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void makeOldPasswordFieldsInvisible() {
        oldPasswordFieldEditTextProfileFragment.setVisibility(View.INVISIBLE);
        oldPasswordFieldLayoutProfileFragment.setVisibility(View.INVISIBLE);
    }



    private void makeOldPasswordFieldsVisible() {
        oldPasswordFieldEditTextProfileFragment.setVisibility(View.VISIBLE);
        oldPasswordFieldLayoutProfileFragment.setVisibility(View.VISIBLE);
    }
    */


    private void setListenerOnUpdateButton() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePopup= new Dialog(getContext());
                updatePopup.setContentView(R.layout.profile_update_popup);
                updatePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConfirmationButton=updatePopup.findViewById(R.id.confirmation_update_profile_button);
                oldPasswordFieldEditTextProfileFragment=updatePopup.findViewById(R.id.old_password_field_edit_text_profile_fragment);
                oldPasswordFieldLayoutProfileFragment=updatePopup.findViewById(R.id.old_password_field_layout);

                //TODO validator fields if valid show

                updatePopup.show();
                dialogConfirmationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateInformation();
                        updatePopup.dismiss();
                        newPasswordFieldEditTextProfileFragment.setText("");
                    }
                });
            }
        });


    }

    private void updateInformation() {
        User newInformation= new User();
        newInformation.setFirstName(firstNameFieldEditTextProfileFragment.getText().toString());
        newInformation.setLastName(lastNameTextInputEditTextProfileFragment.getText().toString());
        newInformation.setEmail(emailTextView.getText().toString());
        profileViewModel.updateInformation(newInformation);
        profileViewModel.updateAuthentication(oldPasswordFieldEditTextProfileFragment.getText().toString(), newPasswordFieldEditTextProfileFragment.getText().toString());
        setDynamicallyValues();

    }

    private void setBindInformation() {

        firstNameFieldEditTextProfileFragment = binding.firstNameFieldEditTextProfileFragment;
        lastNameTextInputEditTextProfileFragment = binding.lastNameFieldEditTextProfileFragment;
        newPasswordFieldEditTextProfileFragment=binding.newPasswordFieldEditTextProfileFragment;
        emailTextView= binding.profileEmail;
        fullNameTextView= binding.profileFullName;

        updateButton= binding.infoUpdateProfileButton;

        setDynamicallyValues();

    }

    private void setDynamicallyValues() {
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), emailTextView::setText);
        profileViewModel.getFirstName().observe(getViewLifecycleOwner(), firstNameFieldEditTextProfileFragment::setText);
        profileViewModel.getLastName().observe(getViewLifecycleOwner(), lastNameTextInputEditTextProfileFragment::setText);
        profileViewModel.getFullName().observe(getViewLifecycleOwner(), fullNameTextView::setText);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}