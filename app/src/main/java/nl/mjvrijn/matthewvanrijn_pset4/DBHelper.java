package nl.mjvrijn.matthewvanrijn_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/* DBHelper manages the data set and its connection to the database. Doing this in one
 * class ensures that the database and data set remain synchronised at all times, so that
 * no additional tasks have to be performed when the app ones or closes. */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todo.db";
    private static final String DB_TABLE = "todo";
    private static final int DB_VERSION = 1;

    private ListViewAdapter adapter;
    private ArrayList<ToDoTask> list;

    /* The constructor of the helper stored the listView adapter to notify it of changes
     * and reads data from the database to the data set on creation. */
    public DBHelper(Context c, ListViewAdapter a, ArrayList<ToDoTask> l) {
        super(c, DB_NAME, null, DB_VERSION);

        adapter = a;
        list = l;

        readTasks();
    }

    /* Read the to-do list from the database and put it in the data set. When finished, notify
     * the adapter of change. */
    public void readTasks() {
        SQLiteDatabase database = getWritableDatabase();
        String query = String.format("SELECT _id , task , done FROM %s", DB_TABLE);
        Cursor c = database.rawQuery(query, null);

        while(c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex("_id"));
            String task = c.getString(c.getColumnIndex("task"));
            boolean done = c.getInt(c.getColumnIndex("done")) == 1;

            list.add(new ToDoTask(id, task, done));
        }

        c.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    /* Add a task given by a string to both the database and the data set and notify the adapter. */
    public void addTask(String task) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues entry = new ContentValues();
        entry.put("task", task);
        entry.put("done", 0);

        long id = database.insert(DB_TABLE, null, entry);
        database.close();

        list.add(new ToDoTask(id, task, false));
        adapter.notifyDataSetChanged();
    }

    /* Remove a given task from both the database and the data set and notify the adapter. */
    public void removeTask(ToDoTask toRemove) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(DB_TABLE, "_id = ?", new String[]{String.valueOf(toRemove.getID())});

        list.remove(toRemove);
        adapter.notifyDataSetChanged();
    }

    /* Toggle the checked status of a task in both the database and the data set and notify
     * the adapter. */
    public void toggleDone(ToDoTask task) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues entry = new ContentValues();
        entry.put("task", task.getTask());

        if(task.isDone()) {
            entry.put("done", 0);
        } else {
            entry.put("done", 1);
        }

        task.setDone(!task.isDone());

        database.update(DB_TABLE, entry, "_id = ?", new String[]{String.valueOf(task.getID())});
        database.close();

        adapter.notifyDataSetChanged();
    }

    /* Function to initialise the DB table when the database is first created. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT , task TEXT , done INTEGER )"
                , DB_TABLE);
        db.execSQL(query);
    }

    /* Function to update the database when a structural change is made. Since none have been made
     * this function does not do anything. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
