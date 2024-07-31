package models;

import java.io.Serial;
import java.io.Serializable;

import exceptions.NullTaskException;

public class Task implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;
	
	private String title;
    private String description;
    private Boolean completed;

    public Task(String title, String description) throws NullTaskException {
        if (title == null || title.isEmpty()) {
            throw new NullTaskException("Título da tarefa não pode ser vazio!");
        }
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isCompleted() { return completed; }

    @Override
    public String toString() {
        return title + ": " + description;
    }
}
