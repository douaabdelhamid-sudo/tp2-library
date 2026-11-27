package library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Copy {
    private final String inventoryNumber;
    private CopyStatus status;
    private final Book book;
    private final List<Loan> loans = new ArrayList<>();

    public Copy(String inventoryNumber, Book book) {
        this.inventoryNumber = Objects.requireNonNull(inventoryNumber, "Inventory number cannot be null");
        this.book = Objects.requireNonNull(book, "Book cannot be null");
        this.status = CopyStatus.AVAILABLE;
        this.book.addCopy(this);
    }

    public boolean isAvailable() {
        return this.status == CopyStatus.AVAILABLE;
    }

    public Loan getCurrentLoan() {
        for (Loan loan : loans) {
            if (loan.getReturnDate() == null) {
                return loan;
            }
        }
        return null;
    }

    public void markLost() {
        if (!isAvailable()) {
            throw new IllegalStateException("Only an available copy can be marked lost.");
        }
        this.status = CopyStatus.LOST;
    }

    void setStatus(CopyStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    void addLoan(Loan loan) {
        if (loan != null && !loans.contains(loan)) {
            loans.add(loan);
        }
    }

    public String getInventoryNumber() { return inventoryNumber; }
    public CopyStatus getStatus() { return status; }
    public Book getBook() { return book; }
    public List<Loan> getLoans() { return Collections.unmodifiableList(loans); }
}