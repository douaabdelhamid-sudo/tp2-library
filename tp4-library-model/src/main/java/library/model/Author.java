package library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Author {
    private final String name;
    private final List<Book> books = new ArrayList<>();

    public Author(String name) {
        this.name = Objects.requireNonNull(name, "Author name cannot be null");
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    // Package visibility so only Book can call it
    void addBook(Book book) {
        if (book != null && !books.contains(book)) {
            books.add(book);
        }
    }
}