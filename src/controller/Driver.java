package controller;

import java.util.Scanner;

import model.Graph;

public class Driver {

	public static void main(String[] args) {
		
		
		long start = System.currentTimeMillis();
		Graph graph = new Graph();
		System.out.println("Graph Build Took: " + String.valueOf((System.currentTimeMillis() - start)/1000) + " seconds");
		graph.mostLinkedTo();
		
		Scanner scanner = new Scanner(System.in);
		boolean run = true;
		while (run) {
			
			System.out.println("Where you would like to start your search?");
			String searchStart = "";
			boolean isValidStart = false;
			while (!isValidStart) {
				searchStart = scanner.nextLine().trim();
				isValidStart = graph.isValidName(searchStart);
				if (!isValidStart) {
					System.out.println("Name not found. Try again.");
				}
			}
			
			System.out.println("Which article would you like to travel to?");
			String searchEnd = "";
			boolean isValidEnd = false;
			while (!isValidEnd) {
				searchEnd = scanner.nextLine().trim();
				isValidEnd = graph.isValidName(searchEnd);
				if (!isValidEnd) {
					System.out.println("Name not found. Try again.");
				}
			}
			
			start = System.currentTimeMillis();
			try {
				System.out.println(graph.search(searchStart, searchEnd));
				System.out.println("Search Took: " + String.valueOf((System.currentTimeMillis() - start)/1000) + " seconds");
			} catch (Exception e){
				System.out.println("System ran into a problem:");
				System.out.println(e.getMessage());
			}
			
			
			
			
			System.out.println("Would you like to search again? (y/n)");
			String input = scanner.nextLine().trim();
			while (!(input.equals("y") || input.equals("n"))) {
				System.out.println("Command not recognized. Try again");
				input = scanner.nextLine().trim();
			}
			if (input.equals("n")) {
				run = false;
			}
			
			
		}
		scanner.close();
		System.out.println("Goodbye");		
	}

}
