# Use Case UC-07: Register a return

* **Primary Actor:** Librarian
* **Secondary Actor:** Mail server
* **Goal in Context:** Register the return of a borrowed copy, close the active loan, check for late returns or pending reservations, and issue a return receipt.
* **Precondition:** The librarian is authenticated in the system.
* **Trigger:** A member brings back a borrowed copy to the library desk.

## Main Success Scenario
1. The librarian initiates the return registration and scans the barcode of the returned copy.
2. The system identifies the copy and verifies that it is currently on loan.
3. The system closes the active loan and updates the copy's status.
4. The system checks if the return date exceeds the due date (if late, extension point *late return* -> *Suspend the member* is triggered).
5. The system checks if the book is reserved by another member (if reserved, the copy is set aside and the *Mail server* notifies the reserving member).
6. The system prints the return receipt (**Print the receipt**).

## Alternative Flows / Exceptions
* **1a. The barcode cannot be read:**
  * 1a1. The librarian manually enters the copy's inventory number.
  * 1a2. The scenario resumes at step 2.
* **2a. The copy is not currently on loan:**
  * 2a1. The system displays an error message indicating the copy is not on loan.
  * 2a2. The use case ends (failure).

## Postconditions
* **Success:** The loan is closed, the copy status is updated (available or reserved/on hold), and a receipt is printed.
* **Failure:** System state remains unchanged.