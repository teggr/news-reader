package dev.rebelcraft.news_reader.feeds;

public class FeedFileSystemKey {

    /**
     * Converts an arbitrary string to a valid filesystem directory name by replacing
     * characters that are typically problematic with underscores, including periods.
     *
     * @param feedId The input string to convert.
     * @return A string that is likely to be a valid and safe directory name.
     */
    public static String fromFeedId(FeedId feedId) {

        String input = feedId.id();

        // 1. Trim leading and trailing whitespace
        String trimmedInput = input.trim();

        // 2. Replace characters that are typically problematic in directory names with underscores.
        //    This includes:
        //    - Forward slash (/) and backslash (\) - used as path separators
        //    - Colon (:) - used in Windows drive letters and after schemes in URLs
        //    - Asterisk (*), question mark (?), double quote ("), less than (<), greater than (>) - used as wildcards or have special meaning in some filesystems
        //    - Pipe (|) - used for piping in some shells
        //    - Period (.) - can sometimes cause issues, especially at the beginning or end of filenames/directory names, and can be confused with file extensions.
        //    - Control characters (using regex to remove them)
        String sanitizedName = trimmedInput.replaceAll("[\\\\/:*?\"<>|.]", "_");
        sanitizedName = sanitizedName.replaceAll("\\p{Cntrl}", ""); // Remove control characters

        // 3. Consider replacing spaces with underscores or hyphens (optional, but often improves readability)
        sanitizedName = sanitizedName.replace(' ', '_');

        // 4. Handle cases where the sanitized name might become empty after replacements
        if (sanitizedName.isEmpty()) {
            return "default_directory"; // Or some other fallback
        }

        // 5. Consider limiting the length of the directory name (optional, depending on filesystem limitations)
        final int MAX_LENGTH = 255; // A common maximum length
        if (sanitizedName.length() > MAX_LENGTH) {
            sanitizedName = sanitizedName.substring(0, MAX_LENGTH);
        }

        // 6. Avoid names that are reserved by the operating system (more complex and OS-dependent)
        //    For simplicity, this example doesn't handle OS-specific reserved names.

        return sanitizedName;
    }

}