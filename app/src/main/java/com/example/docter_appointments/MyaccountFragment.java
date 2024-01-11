package com.example.docter_appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyaccountFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView passwordTextView;
    private TextView mobileTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myaccount, container, false);

        nameTextView = view.findViewById(R.id.profileName);
        emailTextView = view.findViewById(R.id.profileEmail);
        passwordTextView = view.findViewById(R.id.profilePassword);
        mobileTextView = view.findViewById(R.id.profileMobile);

        // Retrieve data from arguments bundle
        Bundle data = getArguments();
        if (data != null) {
            String nameUser = data.getString("nameUser");
            String emailUser = data.getString("emailUser");
            String passwordUser = data.getString("passwordUser");
            String mobileUser = data.getString("mobileUser");

            // Set the data to the TextViews
            nameTextView.setText(nameUser);
            emailTextView.setText(emailUser);
            passwordTextView.setText(passwordUser);
            mobileTextView.setText(mobileUser);
        }

        return view;
    }
}
