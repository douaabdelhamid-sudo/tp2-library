package library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private final Member member;
    private final Copy copy;

    // Package-private constructor: only Member.borrow() can create a Loan
    Loan(Member member, Copy copy, LocalDate loanDate) {
        this.member = Objects.requireNonNull(member);
        this.copy = Objects.requireNonNull(copy);
        this.loanDate = Objects.requireNonNull(loanDate);
        this.dueDate = loanDate.plusDays(14);
        this.returnDate = null;

        this.copy.setStatus(CopyStatus.BORROWED);
        this.member.addLoan(this);
        this.copy.addLoan(this);
    }

    public void close(LocalDate returnDate) {
        Objects.requireNonNull(returnDate, "Return date cannot be null");
        if (this.returnDate != null) {
            throw new IllegalStateException("Loan is already closed.");
        }
        this.returnDate = returnDate;
        this.copy.setStatus(CopyStatus.AVAILABLE);
    }

    public boolean isLate(LocalDate today) {
        Objects.requireNonNull(today, "Date cannot be null");
        LocalDate comparisonDate = (returnDate != null) ? returnDate : today;
        return comparisonDate.isAfter(dueDate);
    }

    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Member getMember() { return member; }
    public Copy getCopy() { return copy; }
}
