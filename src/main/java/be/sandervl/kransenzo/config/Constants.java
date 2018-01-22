package be.sandervl.kransenzo.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "nl";
    public static final String CREDENTIALS_PATH = "/tmp/host/beanstalk-credentials.json";
    public static final String VAT_NUMBER = "BE12 1234 4567 890";

    private Constants() {
    }
}
