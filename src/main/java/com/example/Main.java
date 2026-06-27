package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static int sumOfThree(int a, int b, int c) {
        return a + b + c;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
         
        String dataFile = "notes.json";
        NoteManager manager = new NoteManager();
        manager.loadNotesFromFile(dataFile);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String option = scanner.nextLine().trim();
                switch (option) {
                    case "1" -> addNote(scanner, manager);
                    case "2" -> removeNote(scanner, manager);
                    case "3" -> manager.printNotes();
                    case "4" -> {
                        manager.saveNotesToFile(dataFile);
                        System.out.println("Notes saved to " + dataFile);
                    }
                    case "5" -> {
                        manager.saveNotesToFile(dataFile);
                        System.out.println("Saved and exiting.");
                        running = false;
                    }
                    default -> System.out.println("Please choose a valid option.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nNote Manager");
        System.out.println("1) Add note");
        System.out.println("2) Remove note by title");
        System.out.println("3) View notes");
        System.out.println("4) Save notes");
        System.out.println("5) Save and exit");
        System.out.print("Choose an option: ");
    }

    private static void addNote(Scanner scanner, NoteManager manager) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter content: ");
        String content = scanner.nextLine().trim();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        manager.addNote(new Note(title, content, date));
        System.out.println("Note added.");
    }

    private static void removeNote(Scanner scanner, NoteManager manager) {
        System.out.print("Enter note title to remove: ");
        String title = scanner.nextLine().trim();
        boolean removed = manager.removeNoteByTitle(title);
        if (removed) {
            System.out.println("Removed note(s) with title: " + title);
        } else {
            System.out.println("No note found with that title.");
        }
    }
}
 
