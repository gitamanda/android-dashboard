package com.example.amanda.academicdashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EmailNotesActivity extends AppCompatActivity {

    EditText etRecipient, etSubject, etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etBody = (EditText) findViewById(R.id.etBody);

        Intent intent = getIntent();
        String notes = intent.getStringExtra("courseNotes");
        etBody.setText(notes);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent();
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL  , etRecipient.getText());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText());
                emailIntent.putExtra(Intent.EXTRA_TEXT   , etBody.getText());

                startActivity(emailIntent);
            }
        });
    }

}
