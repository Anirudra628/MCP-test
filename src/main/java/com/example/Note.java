//create a Note class that will have some properties like title, content, and date. It should have a constructor to initialize these properties and getter methods to retrieve their values.

package com.example;

import java.util.Objects;

/**
 * Note
 */
public class Note {
    private String title;
    private String content;
    private String date;

    public Note(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Note)) {
            return false;
        }
        Note other = (Note) obj;
        return Objects.equals(title, other.title)
            && Objects.equals(content, other.content)
            && Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, date);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", title, date, content);
    }
}
