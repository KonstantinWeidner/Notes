package com.example.notes;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        Button button = findViewById(R.id.addNote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.add("");
                arrayAdapter.notifyDataSetChanged();
            }
        });

        notes.add("Welcome!");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //menu that appears
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
            notes.add(note); // Add a duplicate note to the ArrayList
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void deleteNote(int noteID) {
        if (noteID >= 0 && noteID < notes.size()) {
            notes.remove(noteID);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}

