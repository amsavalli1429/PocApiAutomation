package com.bookStore.base;

public class Book {

    private int id;
    private String name;
    private String author;
    private int published_year;
    private String book_summary;

    // Constructor for creating a new book (auto ID)
    public Book(String name, String author, int published_year, String book_summary) {
        this.id = generateId();
        this.name = name;
        this.author = author;
        this.published_year = published_year;
        this.book_summary = book_summary;
    }

    // Constructor for updating a book with specific ID
    public Book(int id, String name, String author, int published_year, String book_summary) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published_year = published_year;
        this.book_summary = book_summary;
    }

    // Generate ID using timestamp
    private int generateId() {
        return (int)(System.currentTimeMillis() % 100000);
    }

    // Getters and Setters (Required for JSON serialization)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublished_year() {
        return published_year;
    }

    public void setPublished_year(int published_year) {
        this.published_year = published_year;
    }

    public String getBook_summary() {
        return book_summary;
    }

    public void setBook_summary(String book_summary) {
        this.book_summary = book_summary;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", published_year=" + published_year +
                ", book_summary='" + book_summary + '\'' +
                '}';
    }
}
