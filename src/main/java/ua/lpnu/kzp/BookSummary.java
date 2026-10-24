package ua.lpnu.kzp;

/**
 * Record для збереження підсумкових статистичних показників каталогу.
 */
public record BookSummary(
    int validCount,
    double avgPages,
    double maxPrice,
    double totalPrice
) {}