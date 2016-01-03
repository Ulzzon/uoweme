package com.example.tobbe.uoweme.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.tobbe.uoweme.Person;
import com.example.tobbe.uoweme.R;

/**
 * Created by TobiasOlsson on 16-01-03.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_TITLE = "section_title";
    private Person user;
    private EditText textName;
    private EditText textPhoneNr;
    private EditText textEmail;
    private WebView profileImage;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_TITLE, "Profile");
        fragment.setArguments(args);

        return fragment;
    }

    public ProfileFragment() {
        user = new Person();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile_page, container, false);


        textName = (EditText) rootView.findViewById(R.id.input_name);
        textPhoneNr = (EditText) rootView.findViewById(R.id.input_phoneNr);
        textEmail = (EditText) rootView.findViewById(R.id.input_email);
        profileImage = (WebView) rootView.findViewById(R.id.profile_picture);

        SharedPreferences preferences = getActivity().getSharedPreferences("AppPref", getActivity().MODE_PRIVATE);
        textName.setText(preferences.getString("name", "No name"));
        textEmail.setText(preferences.getString("email", "No Email"));
        textPhoneNr.setText(preferences.getString("phone", ""));
        String gravatar = preferences.getString("grav", "");

        profileImage.getSettings().setUseWideViewPort(true);
        profileImage.getSettings().setLoadWithOverviewMode(true);
//        profileImage.loadUrl(gravatar);
        profileImage.loadUrl("http://www.gravatar.com/avatar/edb1260aa6f7f77688deee83e0a088f7?s=204&d=monsterid");
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}
