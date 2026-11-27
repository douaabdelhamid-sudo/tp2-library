package library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);
    private Author author;
    private Book book;
    private Copy copy;
    private Member student;

    @BeforeEach
    void setup() {
        author = new Author("First author");
        book = new Book("978-0-00-000001-1", "Software Engineering", 2016, author);
        copy = new Copy("C-001", book);
        student = new StudentMember("M-01", "Student 1", "s1@example.org", "L3");
    }

    @Test
    void bookAndAuthorKnowEachOther() {
        assertTrue(book.getAuthors().contains(author));
        assertTrue(author.getBooks().contains(book));
    }

    @Test
    void newCopyIsAvailableAndKnownByBook() {
        assertTrue(copy.isAvailable());
        assertEquals(CopyStatus.AVAILABLE, copy.getStatus());
        assertTrue(book.getCopies().contains(copy));
        assertEquals(1, book.availableCopies());
    }

    @Test
    void borrowingLinksLoanAndChangesStatus() {
        Loan loan = student.borrow(copy, DAY);
        assertFalse(copy.isAvailable());
        assertEquals(CopyStatus.BORROWED, copy.getStatus());
        assertEquals(loan, copy.getCurrentLoan());
        assertTrue(student.getLoans().contains(loan));
        assertTrue(copy.getLoans().contains(loan));
    }

    @Test
    void studentCannotBorrowSixthCopyWhileTeacherCanBorrowTen() {
        for (int i = 1; i <= 5; i++) {
            Copy c = new Copy("C-10" + i, book);
            student.borrow(c, DAY);
        }
        assertFalse(student.canBorrow());
        Copy sixthCopy = new Copy("C-106", book);
        assertThrows(IllegalStateException.class, () -> student.borrow(sixthCopy, DAY));

        Member teacher = new TeacherMember("T-01", "Teacher 1", "t1@example.org", "CS");
        for (int i = 1; i <= 10; i++) {
            Copy c = new Copy("C-20" + i, book);
            teacher.borrow(c, DAY);
        }
        assertFalse(teacher.canBorrow());
        assertEquals(10, teacher.getLoans().size());
    }

    @Test
    void copyCannotBeBorrowedTwice() {
        student.borrow(copy, DAY);
        Member student2 = new StudentMember("M-02", "Student 2", "s2@example.org", "L3");
        assertThrows(IllegalStateException.class, () -> student2.borrow(copy, DAY));
    }

    @Test
    void closingLoanMakesCopyAvailableAndKeepsHistory() {
        Loan loan = student.borrow(copy, DAY);
        loan.close(DAY.plusDays(5));
        assertTrue(copy.isAvailable());
        assertNull(copy.getCurrentLoan());
        assertTrue(student.getLoans().contains(loan));
        assertTrue(copy.getLoans().contains(loan));
    }

    @Test
    void loanIsLateFromDayAfterDueDate() {
        Loan loan = student.borrow(copy, DAY); // Due date: 2026-10-15
        assertFalse(loan.isLate(DAY.plusDays(14))); // On due date -> not late
        assertTrue(loan.isLate(DAY.plusDays(15)));  // Day after due date -> late
    }

    @Test
    void returnedCollectionsCannotBeModified() {
        assertThrows(UnsupportedOperationException.class, () -> book.getAuthors().clear());
        assertThrows(UnsupportedOperationException.class, () -> student.getLoans().clear());
        assertThrows(UnsupportedOperationException.class, () -> copy.getLoans().clear());
    }

    @Test
    void reservationsAreKeptInOrder() {
        Member teacher = new TeacherMember("T-01", "Teacher 1", "t1@example.org", "CS");
        Reservation r1 = student.reserve(book, DAY);
        Reservation r2 = teacher.reserve(book, DAY.plusDays(1));
        assertEquals(r1, book.firstReservation());
        assertEquals(2, book.getReservations().size());
        assertEquals(r2, book.getReservations().get(1));
    }

    @Test
    void onlyAvailableCopyCanBeMarkedLost() {
        student.borrow(copy, DAY);
        assertThrows(IllegalStateException.class, () -> copy.markLost());

        Copy availableCopy = new Copy("C-002", book);
        availableCopy.markLost();
        assertEquals(CopyStatus.LOST, availableCopy.getStatus());
        assertFalse(availableCopy.isAvailable());
    }

    @Test
    void partE_objectDiagramScenario() {
        Author author2 = new Author("Second author");
        book.addAuthor(author2);
        Copy copy2 = new Copy("C-002", book);

        Loan loan1 = student.borrow(copy, DAY);
        Loan loan2 = student.borrow(copy2, DAY);

        Member teacher = new TeacherMember("T-01", "Teacher 1", "t1@example.org", "CS");
        Reservation res = teacher.reserve(book, DAY.plusDays(1));

        assertEquals(2, book.getAuthors().size());
        assertEquals(2, book.getCopies().size());
        assertEquals(2, student.getLoans().size());
        assertEquals(loan1, copy.getCurrentLoan());
        assertEquals(loan2, copy2.getCurrentLoan());
        assertEquals(res, book.firstReservation());
        assertEquals(teacher, res.getMember());
    }

@Test
void firstReservationReturnsOldestRegardlessOfInsertionOrder() {
    Member teacher = new TeacherMember("T-01", "Teacher 1", "t1@example.org", "CS");
    Reservation later = student.reserve(book, DAY.plusDays(3));
    Reservation older = teacher.reserve(book, DAY.plusDays(1));
    assertEquals(older, book.firstReservation());
    assertEquals(later, book.getReservations().get(1));
}}
