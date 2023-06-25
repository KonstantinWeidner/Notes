package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter<String> arrayAdapter;

    private static final int MAX_WORDS = 3; // Maximale Anzahl von Wörtern in der Vorschau
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNotesFromSharedPreferences();
        listView = findViewById(R.id.listView);

        Button button = findViewById(R.id.addNote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "";
                String content = "";
                EditText titleEditText = findViewById(R.id.Titel);
                if (titleEditText != null) {
                    title = titleEditText.getText().toString();
                }

                EditText contentEditText = findViewById(R.id.editText);
                if (contentEditText != null) {
                    content = contentEditText.getText().toString();
                }

                // Combine the title and content to create a new note
                String newNote = title + "\n" + content;
                notes.add(newNote);
                arrayAdapter.notifyDataSetChanged();
                saveNotesToSharedPreferences();
            }
        });

        notes.add("Welcome!");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);

                String note = notes.get(position);
                String[] lines = note.split("\n");
                String title = lines.length > 0 ? lines[0] : "";
                textView.setText(title);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteID", i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                showContextMenu(i);
                return true;
            }
        });
    }

    private void showContextMenu(final int noteID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new CharSequence[]{"Copy", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                if (choice == 0) {
                    duplicateNote(noteID);
                } else if (choice == 1) {
                    deleteNote(noteID);
                }
            }
        });
        builder.show();
    }

    private void duplicateNote(int noteID) {
        if (noteID >= 0 && noteID < notes.size()) {
            String note = notes.get(noteID);
            notes.add(note);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void deleteNote(int noteID) {
        if (noteID >= 0 && noteID < notes.size()) {
            notes.remove(noteID);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void saveNotesToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("notesSet", new HashSet<>(notes));
        editor.apply();
    }

    private void loadNotesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("notes", Context.MODE_PRIVATE);
        Set<String> notesSet = sharedPreferences.getStringSet("notesSet", new HashSet<>());
        notes = new ArrayList<>(notesSet != null ? notesSet : new HashSet<>());
    }
}
