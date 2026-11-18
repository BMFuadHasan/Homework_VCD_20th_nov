public class Task3 {
    public static int sumDigits(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("The number must be natural (non-negative).");
        }
        if (x < 10) {
            return x;
        }
        return x % 10 + sumDigits(x / 10);
    }
}
