package legacy;

import java.util.Arrays;
import java.util.List;

/**
 * Simulates a Java API that exposes platform types to Kotlin callers.
 */
public final class LegacyApi {
    private LegacyApi() {
    }

    public static List<String> fetchProductCodes() {
        return Arrays.asList("A-001", null, "B-002", "  ", "C-003");
    }

    public static List<String> fetchSanitizedProductCodes() {
        return Arrays.asList("A-100", "B-200", "C-300");
    }
}
