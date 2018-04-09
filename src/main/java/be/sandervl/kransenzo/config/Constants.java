package be.sandervl.kransenzo.config;

import java.time.ZoneId;

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
    public static final String VAT_NUMBER = "BE 71 7332 4014 6811 4";

    public static final ZoneId WORKING_ZONE_ID = ZoneId.of("Europe/Paris");

    private Constants() {
    }
}
