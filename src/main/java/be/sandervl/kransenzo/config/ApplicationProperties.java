package be.sandervl.kransenzo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Kransenzo.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Mail mail = new Mail();

    public Mail getMail() {
        return mail;
    }

    public void setMail( Mail mail ) {
        this.mail = mail;
    }

    public static class Mail
    {
        private String confirmation;

        public String getConfirmation() {
            return confirmation;
        }

        public void setConfirmation( String confirmation ) {
            this.confirmation = confirmation;
        }
    }
}
