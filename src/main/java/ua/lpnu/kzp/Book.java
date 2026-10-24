package ua.lpnu.kzp;

/**
 * Клас-сутність, що представляє книгу в каталозі.
 * Містить перевірку інваріантів у конструкторі та фабричний метод fromCsv.
 */
public class Book {
    private final String title;
    private final String author;
    private final int pages;
    private final double price;

    public Book(String title, String author, int pages, double price) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва книги не може бути порожньою");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор не може бути порожнім");
        }
        if (pages <= 0) {
            throw new IllegalArgumentException("Кількість сторінок повинна бути більше 0: " + pages);
        }
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Некоректна або від'ємна ціна: " + price);
        }
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.price = price;
    }

    /**
     * Статичний фабричний метод для створення об'єкта Book із CSV-рядка.
     */
    public static Book fromCsv(String csvLine) {
        if (csvLine == null || csvLine.isBlank()) {
            throw new IllegalArgumentException("Рядок CSV порожній");
        }
        String[] parts = csvLine.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Очікується 4 поля, отримано: " + parts.length);
        }

        String title = parts[0].trim();
        String author = parts[1].trim();
        int pages = Integer.parseInt(parts[2].trim());
        double price = Double.parseDouble(parts[3].trim());

        return new Book(title, author, pages, price);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public double getPrice() {
        return price;
    }
}