package library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Book {
    private final String isbn;
    private final String title;
    private final int year;
    private final List<Author> authors = new ArrayList<>();
    private final List<Copy> copies = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public Book(String isbn, String title, int year, Author firstAuthor) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.year = year;
        Objects.requireNonNull(firstAuthor, "A book must have at least one author");
        addAuthor(firstAuthor);
    }

    public void addAuthor(Author author) {
        Objects.requireNonNull(author, "Author cannot be null");
        if (!authors.contains(author)) {
            authors.add(author);
            author.addBook(this);
        }
    }

    void addCopy(Copy copy) {
        if (copy != null && !copies.contains(copy)) {
            copies.add(copy);
        }
    }

    void addReservation(Reservation reservation) {
        if (reservation != null && !reservations.contains(reservation)) {
            reservations.add(reservation);
        }
    }

    public int availableCopies() {
        int count = 0;
        for (Copy copy : copies) {
            if (copy.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    public Reservation firstReservation() {
        return reservations.isEmpty() ? null : reservations.get(0);
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public List<Author> getAuthors() { return Collections.unmodifiableList(authors); }
    public List<Copy> getCopies() { return Collections.unmodifiableList(copies); }
    public List<Reservation> getReservations() { return Collections.unmodifiableList(reservations); }
}