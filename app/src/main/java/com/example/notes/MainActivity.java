package com.example.notes;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        Button button = (Button)findViewById(R.id.addNote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.add("");
                arrayAdapter.notifyDataSetChanged();            }
        });

        notes.add("Welcome!");


            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                    Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class); //jump to editor
                    intent.putExtra("noteID", i);
                    startActivity(intent);
                }
            });
    }
}