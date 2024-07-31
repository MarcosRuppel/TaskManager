package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidTaskException;
import exceptions.NullTaskException;
import exceptions.TaskNotFoundException;
import models.Task;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private final GerenciadorDados gerenciadorDados = new GerenciadorDados();
    
    public TaskManager() {
		this.tasks = gerenciadorDados.carregar();
	}

	public void addTask(Task task) {
        tasks.add(task);
        gerenciadorDados.salvar(tasks);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        gerenciadorDados.salvar(tasks);
    }

    public Task getTask(int index) throws TaskNotFoundException {
        if (index > -1 && index < tasks.size()) {
            return tasks.get(index);
        } else {
            throw new TaskNotFoundException("Tarefa não encontrada!!");
        }
    }

    public void exportToCSV(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.println(task.getTitle() + "," + task.getDescription());
            }
        }
    }

    public void importFromCSV(String filePath) throws IOException, InvalidTaskException, NullTaskException {
        tasks.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    tasks.add(new Task(parts[0], parts[1]));
                } else {
                    throw new InvalidTaskException("O arquivo selecionado contém tarefas inválidas.");
                }
            }
            gerenciadorDados.salvar(tasks);
        }
    }

    public List<Task> getAllTasks() {
        return tasks;
    }
}
