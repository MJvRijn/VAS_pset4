package nl.mjvrijn.matthewvanrijn_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todo.db";
    private static final String DB_TABLE = "todo";
    private static final int DB_VERSION = 1;

    private ListViewAdapter adapter;
    private ArrayList<ToDoTask> list;

    public DBHelper(Context c, ListViewAdapter a, ArrayList<ToDoTask> l) {
        super(c, DB_NAME, null, DB_VERSION);

        adapter = a;
        list = l;

        readTasks();
    }

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

    public void removeTask(ToDoTask toRemove) {
        long id = toRemove.getID();
        list.remove(toRemove);

        SQLiteDatabase database = getWritableDatabase();
        database.delete(DB_TABLE, "_id = ?", new String[]{String.valueOf(id)});

        adapter.notifyDataSetChanged();
    }

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT , task TEXT , done INTEGER )", DB_TABLE);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
