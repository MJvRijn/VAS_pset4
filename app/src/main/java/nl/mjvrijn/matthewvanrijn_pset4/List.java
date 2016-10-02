package nl.mjvrijn.matthewvanrijn_pset4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class List extends AppCompatActivity {
    private DBHelper database;
    private ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("To-Do List");

        ArrayList<ToDoTask> list = new ArrayList<>();

        ListView listView = (ListView) findViewById(R.id.list_listview);
        adapter = new ListViewAdapter(this, 0, list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ToDoTask toRemove = adapter.getItem(position);

                new AlertDialog.Builder(List.this)
                        .setTitle("Remove from To-Do list")
                        .setMessage(String.format("Are you sure you want to remove \"%s\" from your To-Do list?"
                                , toRemove.getTask()))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeTask(toRemove);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                database.toggleDone(adapter.getItem(position));
            }
        });

        database = new DBHelper(this, adapter, list);

        final EditText field = (EditText) findViewById(R.id.list_edittext);
        field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTask(findViewById(R.id.list_button));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void addTask(View v) {
        EditText field = (EditText) findViewById(R.id.list_edittext);
        String task = field.getText().toString().trim();
        database.addTask(task);
        field.setText("");
        Toast.makeText(this, String.format("Added \"%s\" to your To-Do list", task), Toast.LENGTH_LONG).show();
    }

    public void removeTask(ToDoTask t) {
        database.removeTask(t);
        Toast.makeText(this, String.format("Removed \"%s\" from your To-Do list", t.getTask()), Toast.LENGTH_LONG).show();
    }
}
