package library;

public class Copy {
    private final String inventoryNumber;
    private final Book book;
    private boolean available = true;

    public Copy(String inventoryNumber, Book book) {
        this.inventoryNumber = inventoryNumber;
        this.book = book;
    }

    public String getInventoryNumber() { return inventoryNumber; }
    public Book getBook() { return book; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}