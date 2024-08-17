package ccb.smonica.recitar_api.util;

public class DateUtil {
    public static String normalizeDayOrMonth(String piece) {
        piece = piece.trim();

        if (piece.length() < 2) {
            piece = String.format("0%s", piece);
        }
        return piece;
    }
}
