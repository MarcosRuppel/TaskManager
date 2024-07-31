package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import models.Task;

public class GerenciadorDados {
	
	@SuppressWarnings("unchecked")
	public List<Task> carregar() {
		try {
			FileInputStream fileInputStream = new FileInputStream("data.bin");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			List<Task> tasks = (List<Task>) objectInputStream.readObject();
			objectInputStream.close();
			return tasks;
		} catch (Exception e) {
			System.out.println("Arquivo data.bin não encontrado, iniciando nova lista...");
		}
		
		return new ArrayList<>();
	}
	
	public void salvar(List<Task> tasks) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("data.bin");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(tasks);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
