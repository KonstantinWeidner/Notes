package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {

    int noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);
        EditText titleEditText = findViewById(R.id.Titel);
        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", -1);

        if (noteID != -1) {
            String note = MainActivity.notes.get(noteID);
            String[] lines = note.split("\n");
            if (lines.length >= 2) {
                String title = lines[0];
                String content = lines[1];
                titleEditText.setText(title);
                editText.setText(content);
            }
        } else {
            MainActivity.notes.add("");
            noteID = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = titleEditText.getText().toString();
                String content = editText.getText().toString();
                String note = title + "\n" + content;
                MainActivity.notes.set(noteID, note);
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        titleEditText.addTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);
    }
}
