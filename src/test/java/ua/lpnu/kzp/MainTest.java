package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testAverageCalculation() {
        int totalPages = 912;
        int count = 3;
        double expectedAverage = 304.0;
        double actualAverage = (double) totalPages / count;

        assertEquals(expectedAverage, actualAverage, 0.0001, "Середнє значення сторінок обчислено некоректно");
    }

    @Test
    void testCsvParsingLogic() {
        String validLine = "Кобзар;Тарас Шевченко;720;450.00";
        String[] fields = validLine.split(";", -1);

        assertEquals(4, fields.length);
        assertEquals("Кобзар", fields[0]);
        assertTrue(Integer.parseInt(fields[2].trim()) > 0);
    }
}
