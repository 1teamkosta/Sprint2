package kr.swdl.sprint2.service.util;

public final class LikePatternEscaper {
    private LikePatternEscaper() {}

    public static String escape(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;

        return keyword.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
