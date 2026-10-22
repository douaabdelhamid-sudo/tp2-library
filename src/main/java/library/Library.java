package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private static final int MAX_LOANS = 5;

    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Copy> copies = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void addCopy(String isbn, String inventoryNumber) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Unknown ISBN: " + isbn);
        }
        copies.put(inventoryNumber, new Copy(inventoryNumber, book));
    }

    public void registerMember(Member member) {
        members.put(member.getNumber(), member);
    }

    public Copy findCopy(String inventoryNumber) {
        return copies.get(inventoryNumber);
    }

    public Loan lend(String memberNumber, String inventoryNumber, LocalDate today) {
        Member member = members.get(memberNumber);
        if (member == null) {
            throw new IllegalArgumentException("Unknown member: " + memberNumber);
        }
        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy: " + inventoryNumber);
        }
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy not available: " + inventoryNumber);
        }
        long openLoans = loans.stream()
                .filter(l -> l.isOpen() && l.getMember().getNumber().equals(memberNumber))
                .count();
        if (openLoans >= MAX_LOANS) {
            throw new IllegalStateException("Too many loans for member: " + memberNumber);
        }
        Loan loan = new Loan(member, copy, today);
        loans.add(loan);
        copy.setAvailable(false);
        return loan;
    }

    public Loan returnCopy(String inventoryNumber, LocalDate today) {
        Copy copy = copies.get(inventoryNumber);
        if (copy == null) {
            throw new IllegalArgumentException("Unknown copy: " + inventoryNumber);
        }
        Loan loan = loans.stream()
                .filter(l -> l.isOpen() && l.getCopy().equals(copy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Copy is not on loan: " + inventoryNumber));
        loan.close(today);
        copy.setAvailable(true);
        return loan;
    }

    public List<Book> searchByTitle(String keyword) {
        String k = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(k))
                .toList();
    }
}