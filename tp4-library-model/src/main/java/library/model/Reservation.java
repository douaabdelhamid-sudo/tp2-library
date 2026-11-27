package library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final LocalDate date;
    private final Member member;
    private final Book book;

    // Package-private constructor: created via Member.reserve()
    Reservation(Member member, Book book, LocalDate date) {
        this.member = Objects.requireNonNull(member);
        this.book = Objects.requireNonNull(book);
        this.date = Objects.requireNonNull(date);

        this.member.addReservation(this);
        this.book.addReservation(this);
    }

    public LocalDate getDate() { return date; }
    public Member getMember() { return member; }
    public Book getBook() { return book; }
}