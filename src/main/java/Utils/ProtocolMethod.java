package Utils;

/**
 * Created by Dominika Salawa & Pawel Polit
 */

public enum ProtocolMethod {
    GET,
    POST,
    DELETE;

    @Override
    public String toString() {
        return name() + " ";
    }
}
