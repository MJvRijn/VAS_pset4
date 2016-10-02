package nl.mjvrijn.matthewvanrijn_pset4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/* List.java is the main and only activity of the app. */
public class List extends AppCompatActivity {
    private DBHelper database;
    private ListViewAdapter adapter;

    /* Set up the app */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("To-Do List");

        // Initialise the data, adapter and database helper.
        ArrayList<ToDoTask> list = new ArrayList<>();

        ListView listView = (ListView) findViewById(R.id.list_listview);
        adapter = new ListViewAdapter(this, 0, list);
        listView.setAdapter(adapter);
        database = new DBHelper(this, adapter, list);

        // Set up the listeners
        setUpListeners(listView);
    }

    /* Define and set listeners and actions for item presses, long presses and keyboard enter presses. */
    public void setUpListeners(ListView listView) {

        // On a long press; display a confirmation dialog box which calls removeTask on yes
        // and does nothing on no.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ToDoTask toRemove = adapter.getItem(position);

                new AlertDialog.Builder(List.this)
                    .setTitle(getResources().getString(R.string.list_remove_dialog_title))
                    .setMessage(String.format(getResources().getString(R.string.list_remove_dialog_message),
                            toRemove.getTask()))
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
                    }).show();

                return true;
            }
        });

        // On a normal press; check the task off.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                database.toggleDone(adapter.getItem(position));
            }
        });

        // When the enter is pressed on the keyboard; add the task in the edittext field.
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

    /* Create a task given by the contents of the EditText field, and add it to the list. */
    public void addTask(View v) {
        EditText field = (EditText) findViewById(R.id.list_edittext);
        String task = field.getText().toString().trim();

        // Do not add empty tasks
        if(!task.equals("")) {
            database.addTask(task);
            field.setText("");
            Toast.makeText(this, String.format(getResources().getString(R.string.list_add_toast),
                    task), Toast.LENGTH_LONG).show();
        }
    }

    /* Remove a given task from the list. */
    public void removeTask(ToDoTask t) {
        database.removeTask(t);
        Toast.makeText(this, String.format(getResources().getString(R.string.list_remove_toast),
                t.getTask()), Toast.LENGTH_LONG).show();
    }
}
