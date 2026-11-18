import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task2 {
    public static long daysBetween(
            int yearFrom, int monthFrom, int dayFrom,
            int yearTo, int monthTo, int dayTo) {
        LocalDate from = LocalDate.of(yearFrom, monthFrom, dayFrom);
        LocalDate to = LocalDate.of(yearTo, monthTo, dayTo);
        return Math.abs(ChronoUnit.DAYS.between(from, to));
    }
}
