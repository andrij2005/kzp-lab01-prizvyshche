package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас програми для обробки каталогу книжок.
 * Використовує клас Book та record BookSummary для обробки даних.
 */
public final class Main {

    private Main() {
        // Приватний конструктор для службового класу
    }

    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equalsIgnoreCase(args[i])) {
                printHelp();
                return;
            } else if ("--input".equalsIgnoreCase(args[i]) && i + 1 < args.length) {
                inputPath = Path.of(args[++i]);
            } else if ("--output".equalsIgnoreCase(args[i]) && i + 1 < args.length) {
                outputPath = Path.of(args[++i]);
            }
        }

        if (!Files.exists(inputPath)) {
            System.err.println("Помилка: Вхідний файл не знайдено за шляхом: " + inputPath);
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
            return;
        }

        List<Book> books = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                Book book = Book.fromCsv(line);
                books.add(book);
            } catch (Exception e) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: %s", i + 1, e.getMessage()));
            }
        }

        int validCount = books.size();
        int totalPages = 0;
        double totalPrice = 0.0;
        double maxPrice = validCount > 0 ? Double.NEGATIVE_INFINITY : 0.0;

        for (Book book : books) {
            totalPages += book.getPages();
            totalPrice += book.getPrice();
            if (book.getPrice() > maxPrice) {
                maxPrice = book.getPrice();
            }
        }

        if (validCount == 0) {
            maxPrice = 0.0;
        }

        double avgPages = validCount == 0 ? 0.0 : (double) totalPages / validCount;

        BookSummary summary = new BookSummary(validCount, avgPages, maxPrice, totalPrice);

        StringBuilder sb = new StringBuilder();
        sb.append("=== ЗВІТ ОБРОБКИ КАТАЛОГУ КНИЖОК ===").append(System.lineSeparator());
        sb.append("Вхідний файл: ").append(inputPath).append(System.lineSeparator());
        sb.append("Вихідний файл: ").append(outputPath).append(System.lineSeparator()).append(System.lineSeparator());

        if (!errors.isEmpty()) {
            sb.append("ЗНАЙДЕНО ПОМИЛКИ В ДАНИХ (").append(errors.size()).append("):").append(System.lineSeparator());
            for (String err : errors) {
                sb.append(" - ").append(err).append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }

        sb.append(String.format(Locale.ROOT, "РЕЗУЛЬТАТИ СТАТИСТИКИ (коректних записів: %d):%n", summary.validCount()));
        sb.append(String.format(Locale.ROOT, " - Кількість коректних книжок: %d%n", summary.validCount()));
        sb.append(String.format(Locale.ROOT, " - Середня кількість сторінок: %.2f%n", summary.avgPages()));
        sb.append(String.format(Locale.ROOT, " - Найдорожча книга (максимальна ціна): %.2f грн%n", summary.maxPrice()));
        sb.append(String.format(Locale.ROOT, " - Загальна вартість каталогу: %.2f грн%n", summary.totalPrice()));
        sb.append("=======================================").append(System.lineSeparator());

        String reportText = sb.toString();
        System.out.print(reportText);

        try {
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }
            Files.writeString(outputPath, reportText, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Помилка запису звіту: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Використання: java -jar lab01.jar [параметри]");
        System.out.println("Параметри:");
        System.out.println("  --input <файл>   Шлях до вхідного CSV файлу");
        System.out.println("  --output <файл>  Шлях до вихідного файлу звіту");
        System.out.println("  --help           Показати цю підказку");
    }
}