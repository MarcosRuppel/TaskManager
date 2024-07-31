package UI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.TaskManager;
import exceptions.*;
import models.Task;

public class TaskManagerGUI extends JFrame {
	/*
	 * Usado para deixar o objeto incompativel caso vc mude a estrutura do projeto.
	 * O JFrame obriga implementar o serial ID, então foi implementado
	 * um serial default para evitar warnings durante a compilação
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	
	private TaskManager taskManager;
    private DefaultListModel<String> taskListModel;

    public TaskManagerGUI() {
        inicializarInterface();
    }

	private void inicializarInterface() {
		taskManager = new TaskManager();
        taskListModel = new DefaultListModel<>();

        setTitle("Gerenciador de Tarefas 1.0");
        setSize(800, 600);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/tasks.png"))).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        carregarListModel();

        // criando os componentes da interf
        JTextField titleField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 20);
        JButton addButton = new JButton("Adicionar a Lista");
        JButton removeButton = new JButton("Remover da Lista");
        JButton exportButton = new JButton("Exportar para CSV");
        JButton importButton = new JButton("Importar do CSV");
        if (taskListModel == null) throw new AssertionError();
        JList<String> taskList = new JList<>(Objects.requireNonNull(taskListModel));

        // Adicionando ícones aos botões
        exportButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/export.png")))); // Caminho do ícone de salvar
        importButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/open-file.png")))); // Caminho do ícone de abrir
        addButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/add-task.png"))));
        removeButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/remove-task.png"))));

        // Layout da interface
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Título:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Descrição:"));
        inputPanel.add(new JScrollPane(descriptionArea));
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        // Painel das tarefas
        JPanel controlPanel = new JPanel();
        controlPanel.add(exportButton);
        controlPanel.add(importButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Event Handlers
        addButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                Task task = new Task(title, description);
                taskManager.addTask(task);
                taskListModel.addElement(task.toString());
                titleField.setText("");
                descriptionArea.setText("");

            } catch (NullTaskException ex) {
                JOptionPane.showMessageDialog(TaskManagerGUI.this, ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            try {
                Task task = taskManager.getTask(selectedIndex);
                taskManager.removeTask(task);
                taskListModel.removeElementAt(selectedIndex);
            } catch (TaskNotFoundException ex) {
                // Mostrar uma mensagem de erro se nada estiver selecionado
                JOptionPane.showMessageDialog(TaskManagerGUI.this,
                        "Por favor, selecione um item para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(TaskManagerGUI.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    String filePath = fileChooser.getSelectedFile().getPath();
                    taskManager.exportToCSV(filePath);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(TaskManagerGUI.this,
                            "Erro ao salvar tarefas: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(TaskManagerGUI.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    String filePath = fileChooser.getSelectedFile().getPath();
                    taskManager.importFromCSV(filePath);
                    carregarListModel();
                } catch (IOException | NullTaskException | InvalidTaskException ex) {
                    JOptionPane.showMessageDialog(TaskManagerGUI.this,
                            "Erro ao carregar tarefas: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
	}

	private void carregarListModel() {
		List<Task> allTasks = taskManager.getAllTasks();
		taskListModel.clear();
		
        for (Task task : allTasks) {
        	taskListModel.addElement(task.toString());
		}
	}
}
