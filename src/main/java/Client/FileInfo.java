package Client;

/**
 * Created by Dominika Salawa & Pawel Polit
 */

public class FileInfo {
    public static FileInfo create(String p_info) {
        String[] info = p_info.split(" ");
        return new FileInfo(info[0], info[1], info[2]);
    }

    private final String name;
    private final String lastModificationDate;
    private final String sha;

    private FileInfo(String p_name, String p_lastModificationDate, String p_sha) {
        name = p_name;
        lastModificationDate = p_lastModificationDate;
        sha = p_sha;
    }

    public String getName() {
        return name;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public String getSha() {
        return sha;
    }
}
