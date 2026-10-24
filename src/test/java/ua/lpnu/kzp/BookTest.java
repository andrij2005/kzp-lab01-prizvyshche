package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тести для перевірки класу Book та record BookSummary.
 */
class BookTest {

    @Test
    void testValidBookCreation() {
        Book book = new Book("Кобзар", "Тарас Шевченко", 720, 450.00);
        assertEquals("Кобзар", book.getTitle());
        assertEquals("Тарас Шевченко", book.getAuthor());
        assertEquals(720, book.getPages());
        assertEquals(450.00, book.getPrice(), 0.0001);
    }

    @Test
    void testConstructorInvariantsValidation() {
        // Порожня назва
        assertThrows(IllegalArgumentException.class, () -> new Book("", "Автор", 100, 100.0));
        
        // Порожній автор
        assertThrows(IllegalArgumentException.class, () -> new Book("Назва", "   ", 100, 100.0));
        
        // Від'ємні або нульові сторінки
        assertThrows(IllegalArgumentException.class, () -> new Book("Назва", "Автор", 0, 100.0));
        assertThrows(IllegalArgumentException.class, () -> new Book("Назва", "Автор", -10, 100.0));
        
        // Від'ємна ціна або нескінченність
        assertThrows(IllegalArgumentException.class, () -> new Book("Назва", "Автор", 100, -50.0));
        assertThrows(IllegalArgumentException.class, () -> new Book("Назва", "Автор", 100, Double.NaN));
    }

    @Test
    void testFromCsvValidLine() {
        Book book = Book.fromCsv("Місто;Валер'ян Підмогильний;320;310.00");
        assertNotNull(book);
        assertEquals("Місто", book.getTitle());
        assertEquals("Валер'ян Підмогильний", book.getAuthor());
        assertEquals(320, book.getPages());
        assertEquals(310.00, book.getPrice(), 0.0001);
    }

    @Test
    void testFromCsvInvalidLine() {
        // Невірна кількість колонок
        assertThrows(IllegalArgumentException.class, () -> Book.fromCsv("Назва;Автор;100"));
        
        // Текст замість числа у сторінках
        assertThrows(IllegalArgumentException.class, () -> Book.fromCsv("Назва;Автор;помилка;100.0"));
    }

    @Test
    void testBookSummaryRecord() {
        BookSummary summary = new BookSummary(3, 410.67, 450.00, 980.50);
        assertEquals(3, summary.validCount());
        assertEquals(410.67, summary.avgPages(), 0.0001);
        assertEquals(450.00, summary.maxPrice(), 0.0001);
        assertEquals(980.50, summary.totalPrice(), 0.0001);
    }
}