package library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Member {
    private final String memberNumber;
    private final String name;
    private final String email;
    private final List<Loan> loans = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public Member(String memberNumber, String name, String email) {
        this.memberNumber = Objects.requireNonNull(memberNumber);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
    }

    public abstract int maxLoans();

    public boolean canBorrow() {
        long activeLoans = loans.stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();
        return activeLoans < maxLoans();
    }

    public Loan borrow(Copy copy, LocalDate loanDate) {
        Objects.requireNonNull(copy, "Copy cannot be null");
        Objects.requireNonNull(loanDate, "Loan date cannot be null");
        if (!canBorrow()) {
            throw new IllegalStateException("Member has reached the maximum number of loans.");
        }
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy is not available for borrowing.");
        }
        return new Loan(this, copy, loanDate);
    }

    public Reservation reserve(Book book, LocalDate date) {
        Objects.requireNonNull(book, "Book cannot be null");
        Objects.requireNonNull(date, "Reservation date cannot be null");
        return new Reservation(this, book, date);
    }

    void addLoan(Loan loan) {
        if (loan != null && !loans.contains(loan)) {
            loans.add(loan);
        }
    }

    void addReservation(Reservation reservation) {
        if (reservation != null && !reservations.contains(reservation)) {
            reservations.add(reservation);
        }
    }

    public String getMemberNumber() { return memberNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Loan> getLoans() { return Collections.unmodifiableList(loans); }
    public List<Reservation> getReservations() { return Collections.unmodifiableList(reservations); }
}