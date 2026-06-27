//create a NoteManager class that manages a list of notes, it will add / remove and view notes from CLI or user input. The added or removed notes will be persisted in a file so that they can be retrieved later. The NoteManager class will have methods to add, remove, and view notes. It will also have a method to save the notes to a file and load them from a file.
package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NoteManager {
    private final List<Note> notes;

    public NoteManager() {
        notes = new ArrayList<>();
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public boolean removeNote(Note note) {
        return notes.remove(note);
    }

    public boolean removeNoteByTitle(String title) {
        return notes.removeIf(note -> note.getTitle().equalsIgnoreCase(title));
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    public String viewNotes() {
        if (notes.isEmpty()) {
            return "No notes available.";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            builder.append(i + 1)
                   .append(". ")
                   .append(note.getTitle())
                   .append(" (")
                   .append(note.getDate())
                   .append(")\n")
                   .append(note.getContent())
                   .append("\n\n");
        }
        return builder.toString().trim();
    }

    public void printNotes() {
        System.out.println(viewNotes());
    }

    public void saveNotesToFile(String filename) {
        Path path = Paths.get(filename);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, toJson(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save notes to file: " + filename, e);
        }
    }

    public void loadNotesFromFile(String filename) {
        Path path = Paths.get(filename);
        notes.clear();
        if (!Files.exists(path)) {
            return;
        }

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return;
            }
            notes.addAll(parseJsonArray(content));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load notes from file: " + filename, e);
        }
    }

    private String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < notes.size(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(noteToJson(notes.get(i)));
        }
        builder.append("]");
        return builder.toString();
    }

    private static String noteToJson(Note note) {
        return '{'
            + "\"title\":" + jsonString(note.getTitle())
            + ",\"content\":" + jsonString(note.getContent())
            + ",\"date\":" + jsonString(note.getDate())
            + '}';
    }

    private static String jsonString(String value) {
        return '"' + escapeJson(value == null ? "" : value) + '"';
    }

    private static String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\b': escaped.append("\\b"); break;
                case '\f': escaped.append("\\f"); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                default: escaped.append(c);
            }
        }
        return escaped.toString();
    }

    private static List<Note> parseJsonArray(String json) {
        List<Note> result = new ArrayList<>();
        int index = skipWhitespace(json, 0);
        if (index >= json.length() || json.charAt(index) != '[') {
            return result;
        }
        index++;

        while (index < json.length()) {
            index = skipWhitespace(json, index);
            if (index >= json.length() || json.charAt(index) == ']') {
                break;
            }
            if (json.charAt(index) == ',') {
                index++;
                continue;
            }
            int end = findClosingBrace(json, index);
            String objectJson = json.substring(index, end + 1);
            result.add(parseNoteObject(objectJson));
            index = end + 1;
        }

        return result;
    }

    private static Note parseNoteObject(String objectJson) {
        Map<String, String> values = parseJsonObject(objectJson);
        return new Note(
            values.getOrDefault("title", ""),
            values.getOrDefault("content", ""),
            values.getOrDefault("date", "")
        );
    }

    private static Map<String, String> parseJsonObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        int index = skipWhitespace(json, 0);
        if (index >= json.length() || json.charAt(index) != '{') {
            return result;
        }
        index++;

        while (index < json.length()) {
            index = skipWhitespace(json, index);
            if (index >= json.length() || json.charAt(index) == '}') {
                break;
            }
            if (json.charAt(index) == ',') {
                index++;
                continue;
            }

            ParseResult keyResult = parseJsonString(json, index);
            index = skipWhitespace(json, keyResult.nextIndex);
            if (index >= json.length() || json.charAt(index) != ':') {
                break;
            }
            index++;
            ParseResult valueResult = parseJsonString(json, skipWhitespace(json, index));
            result.put(keyResult.value, valueResult.value);
            index = valueResult.nextIndex;
        }

        return result;
    }

    private static ParseResult parseJsonString(String json, int index) {
        index = skipWhitespace(json, index);
        if (index >= json.length() || json.charAt(index) != '"') {
            throw new IllegalArgumentException("Invalid JSON string at " + index);
        }
        index++;
        StringBuilder builder = new StringBuilder();
        boolean escaped = false;

        while (index < json.length()) {
            char c = json.charAt(index);
            if (escaped) {
                switch (c) {
                    case '"': builder.append('"'); break;
                    case '\\': builder.append('\\'); break;
                    case '/': builder.append('/'); break;
                    case 'b': builder.append('\b'); break;
                    case 'f': builder.append('\f'); break;
                    case 'n': builder.append('\n'); break;
                    case 'r': builder.append('\r'); break;
                    case 't': builder.append('\t'); break;
                    case 'u':
                        if (index + 4 < json.length()) {
                            String hex = json.substring(index + 1, index + 5);
                            builder.append((char) Integer.parseInt(hex, 16));
                            index += 4;
                        }
                        break;
                    default: builder.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                return new ParseResult(builder.toString(), index + 1);
            } else {
                builder.append(c);
            }
            index++;
        }

        throw new IllegalArgumentException("Unterminated JSON string starting at " + index);
    }

    private static int findClosingBrace(String json, int startIndex) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = startIndex; i < json.length(); i++) {
            char c = json.charAt(i);
            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    inString = false;
                }
            } else {
                if (c == '"') {
                    inString = true;
                } else if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        return i;
                    }
                }
            }
        }

        throw new IllegalArgumentException("Unterminated JSON object starting at " + startIndex);
    }

    private static int skipWhitespace(String json, int index) {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
        return index;
    }

    private static final class ParseResult {
        private final String value;
        private final int nextIndex;

        private ParseResult(String value, int nextIndex) {
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }
}
