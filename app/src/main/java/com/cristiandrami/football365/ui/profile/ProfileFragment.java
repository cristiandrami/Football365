package com.cristiandrami.football365.ui.profile;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.databinding.FragmentProfileBinding;
import com.cristiandrami.football365.model.AppUtilities;
import com.cristiandrami.football365.model.registration.NameValidator;
import com.cristiandrami.football365.model.registration.PasswordValidator;
import com.cristiandrami.football365.model.user.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * This class is used as a Controller for the Profile Fragment
 * It contains all methods to manage the Profile Fragment Graphic.
 * It contains an object used to manage the Model of Profile Fragment
 *
 * @author Cristian D. Dramisino
 * @see ProfileViewModel
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

    private ImageView themeIcon;

    private Switch switchThemeButton;

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


        setGraphicalBinding();
        createUpdatePopup();


        setListenerOnUpdateButton();
        setChangeListenerToNewPassword();
        setOnClickListenerSwitchThemeButton();
        setGraphicalValuesDynamically();

        setThemeIconByUserPreference();


        return root;
    }

    private void createUpdatePopup() {
        updatePopup = new Dialog(getContext());
        updatePopup.setContentView(R.layout.profile_update_popup);
        updatePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogConfirmationButton = updatePopup.findViewById(R.id.confirmation_update_profile_button);
        oldPasswordFieldEditTextProfileFragment = updatePopup.findViewById(R.id.profile_update_popup_old_password_field_edit_text_profile_fragment);
        setOnChangeListenerOnTextInputEdit(oldPasswordFieldEditTextProfileFragment, oldPasswordFieldLayoutProfileFragment);
        oldPasswordFieldLayoutProfileFragment = updatePopup.findViewById(R.id.profile_update_popup_old_password_field_layout);

        dialogConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInformation();
            }
        });
    }

    private void setOnChangeListenerOnTextInputEdit(TextInputEditText textField, TextInputLayout textInputLayout) {
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setThemeIconByUserPreference() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String actualTheme=preferences.getString(AppUtilities.THEME_PREFERENCE_KEY, "");

        if(actualTheme.equals(AppUtilities.DARK_THEME)){
            themeIcon.setImageResource(R.drawable.dark_theme_icon);
        }else{
            themeIcon.setImageResource(R.drawable.light_theme_icon);
        }
    }

    private void setChangeListenerToNewPassword() {
        setOnChangeListenerOnTextInputEdit(newPasswordFieldEditTextProfileFragment, binding.profileFragmentNewPasswordFieldLayout);
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

                binding.profileFragmentLastNameFieldLayout.setError(null);
                binding.profileFragmentFirstNameFieldLayout.setError(null);


                String newPassword=newPasswordFieldEditTextProfileFragment.getText().toString().trim();
                String firstName=firstNameFieldEditTextProfileFragment.getText().toString().trim();
                String lastName=lastNameTextInputEditTextProfileFragment.getText().toString().trim();
                boolean areAllFieldCorrect=true;
                if (!newPassword.isEmpty() && !PasswordValidator.validatePassword(newPassword)) {
                    setErrorOnNewPassword();
                    areAllFieldCorrect=false;
                }
                if(lastName==null || !NameValidator.validateName(lastName)){
                    setErrorOnLastName();
                    areAllFieldCorrect=false;
                }
                if(firstName==null || !NameValidator.validateName(firstName)){
                    setErrorOnFirstName();
                    areAllFieldCorrect=false;
                }
                if( areAllFieldCorrect){
                    updatePopup.show();
                }
            }
        });



    }

    private void setErrorOnLastName() {
        binding.profileFragmentLastNameFieldLayout.setError(getActivity().getString(R.string.last_name_empty));
    }
    private void setErrorOnFirstName() {
        binding.profileFragmentFirstNameFieldLayout.setError(getActivity().getString(R.string.first_name_empty));
    }

    public void dismissPopup() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePopup.dismiss();
                newPasswordFieldEditTextProfileFragment.setText("");
                oldPasswordFieldEditTextProfileFragment.setText("");
            }
        });

    }

    private void updateInformation() {
        User newInformation = new User();
        newInformation.setFirstName(firstNameFieldEditTextProfileFragment.getText().toString());
        newInformation.setLastName(lastNameTextInputEditTextProfileFragment.getText().toString());
        newInformation.setEmail(emailTextView.getText().toString());

        String oldPassword = oldPasswordFieldEditTextProfileFragment.getText().toString();
        String newPassword = newPasswordFieldEditTextProfileFragment.getText().toString();


        profileViewModel.updateInformation(newInformation, oldPassword, newPassword, this);
        setGraphicalValuesDynamically();


    }

    private void setGraphicalBinding() {

        firstNameFieldEditTextProfileFragment = binding.profileFragmentFirstNameFieldEditText;
        lastNameTextInputEditTextProfileFragment = binding.profileFragmentLastNameFieldEditText;
        newPasswordFieldEditTextProfileFragment = binding.profileFragmentNewPasswordFieldEditText;
        emailTextView = binding.profileEmail;
        fullNameTextView = binding.profileFullName;

        updateButton = binding.infoUpdateProfileButton;
        switchThemeButton= binding.switchThemeProfileFragment;
        themeIcon= binding.themeIconProfileFragment;



    }

    private void setOnClickListenerSwitchThemeButton() {

        switchThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String actualTheme=preferences.getString(AppUtilities.THEME_PREFERENCE_KEY, "");
                SharedPreferences.Editor editor = preferences.edit();

                if(actualTheme.equals(AppUtilities.DARK_THEME)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    setNewThemePreference(editor, AppUtilities.LIGHT_THEME, R.drawable.light_theme_icon);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setNewThemePreference(editor, AppUtilities.DARK_THEME, R.drawable.dark_theme_icon);

                }
            }
        });
    }

    private void setNewThemePreference(SharedPreferences.Editor editor, String newTheme, int drawableID) {
        editor.remove(AppUtilities.THEME_PREFERENCE_KEY);
        editor.putString(AppUtilities.THEME_PREFERENCE_KEY, newTheme);
        editor.apply();
        themeIcon.setImageResource(drawableID);

    }

    private void setGraphicalValuesDynamically() {
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

    public void setErrorOnNewPassword() {
        binding.profileFragmentNewPasswordFieldLayout.setError(getActivity().getString(R.string.password_not_valid));
    }

    public void setErrorOnOldPassword() {
        oldPasswordFieldLayoutProfileFragment.setError(getActivity().getString(R.string.password_not_correct));
    }
}