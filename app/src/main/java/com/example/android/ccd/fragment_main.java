package com.example.android.ccd;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ccd.R;

public class fragment_main extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Button mEmailSignInButton = (Button) rootView.findViewById(R.id.buttonLoadPicture);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Main2Activity.class);
                i.putExtra("Name", ((EditText) rootView.findViewById(R.id.name)).getText().toString());
                i.putExtra("Password", ((EditText) rootView.findViewById(R.id.password)).getText().toString());
                i.putExtra("id", ((EditText) rootView.findViewById(R.id.email)).getText().toString());
                i.putExtra("ISEMPLOYER", ((RadioButton) rootView.findViewById(R.id.radio_employer)).isChecked());


                startActivity(i);
                getActivity().finish();

            }

        });

        return rootView;

    }
}