package nl.mjvrijn.matthewvanrijn_pset4;

/* A data class to hold information about a task. Should be self-explanatory. */

public class ToDoTask {
    private long id;
    private String task;
    private boolean done;

    public ToDoTask(long id, String task, boolean done) {
        this.id = id;
        this.task = task;
        this.done = done;
    }

    public long getID() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean d) {
        done = d;
    }
}
