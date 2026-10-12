package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас консольної програми обробки даних каталогу книжок.
 */
public final class Main {

    private Main() {
        // Заборона створення екземплярів службового класу
    }

    /**
     * Точка входу в програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equals(args[i])) {
                System.out.printf("Використання: java -jar lab01.jar [--help] [--input <файл>] [--output <файл>]%n");
                return;
            }
            if ("--input".equals(args[i]) && i + 1 < args.length) {
                inputPath = Path.of(args[i + 1]);
            }
            if ("--output".equals(args[i]) && i + 1 < args.length) {
                outputPath = Path.of(args[i + 1]);
            }
        }

        if (!Files.exists(inputPath)) {
            System.out.printf(Locale.ROOT, "Помилка: вхідний файл не знайдено за шляхом: %s%n", inputPath);
            return;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.printf(Locale.ROOT, "Помилка читання файла: %s%n", e.getMessage());
            return;
        }

        List<String> errors = new ArrayList<>();
        int validCount = 0;
        int totalPages = 0;
        double totalPrice = 0.0;
        double maxPrice = Double.NEGATIVE_INFINITY;

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] fields = line.split(";", -1);
            if (fields.length != 4) {
                errors.add("Рядок %d: очікується 4 поля, отримано %d".formatted(index + 1, fields.length));
                continue;
            }

            if (fields[0].isBlank() || fields[1].isBlank()) {
                errors.add("Рядок %d: порожня назва книжки або автор".formatted(index + 1));
                continue;
            }

            try {
                int pages = Integer.parseInt(fields[2].trim());
                double price = Double.parseDouble(fields[3].trim());

                if (pages <= 0 || price < 0) {
                    errors.add("Рядок %d: від'ємна або нульова кількість сторінок/ціна".formatted(index + 1));
                    continue;
                }

                validCount++;
                totalPages += pages;
                totalPrice += price;
                maxPrice = Math.max(maxPrice, price);

            } catch (NumberFormatException e) {
                errors.add("Рядок %d: нечислове значення у числових полях".formatted(index + 1));
            }
        }

        double avgPages = validCount == 0 ? 0.0 : (double) totalPages / validCount;
        double actualMaxPrice = validCount == 0 ? 0.0 : maxPrice;

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("=== ЗВІТ ОБРОБКИ КАТАЛОГУ КНИЖОК ===%n".formatted());
        reportBuilder.append(String.format(Locale.ROOT, "Оброблено файлів: %s%n", inputPath));
        reportBuilder.append(String.format(Locale.ROOT, "1. Коректних записів: %d%n", validCount));
        reportBuilder.append(String.format(Locale.ROOT, "2. Середня кількість сторінок: %.2f%n", avgPages));
        reportBuilder.append(String.format(Locale.ROOT, "3. Найдорожча книга: %.2f грн%n", actualMaxPrice));
        reportBuilder.append(String.format(Locale.ROOT, "4. Загальна вартість каталогу: %.2f грн%n", totalPrice));
        reportBuilder.append(String.format(Locale.ROOT, "Помилок при обробці: %d%n", errors.size()));

        for (String err : errors) {
            reportBuilder.append(" - %s%n".formatted(err));
        }

        String finalReport = reportBuilder.toString();
        System.out.print(finalReport);

        try {
            Path parentDir = outputPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.writeString(outputPath, finalReport, StandardCharsets.UTF_8);
            System.out.printf(Locale.ROOT, "Звіт успішно збережено у файл: %s%n", outputPath);
        } catch (IOException e) {
            System.out.printf(Locale.ROOT, "Не вдалося зберегти звіт у файл: %s%n", e.getMessage());
        }
    }
}
