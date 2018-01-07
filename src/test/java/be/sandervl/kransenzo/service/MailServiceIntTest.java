package be.sandervl.kransenzo.service;

import be.sandervl.kransenzo.KransenzoApp;
import be.sandervl.kransenzo.config.ApplicationProperties;
import be.sandervl.kransenzo.config.Constants;
import be.sandervl.kransenzo.domain.Customer;
import be.sandervl.kransenzo.domain.Order;
import be.sandervl.kransenzo.domain.Product;
import be.sandervl.kransenzo.domain.User;
import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import io.github.jhipster.config.JHipsterProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KransenzoApp.class)
public class MailServiceIntTest {

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor messageCaptor;

    private MailService mailService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService = new MailService( jHipsterProperties, applicationProperties, javaMailSender, messageSource,
                                       templateEngine );
    }

    @Test
    public void testSendEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendMultipartEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).isEqualTo("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendMultipartHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, true);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).isEqualTo("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendEmailFromTemplate() throws Exception {
        User user = new User();
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        user.setLangKey("en");
        mailService.sendEmailFromTemplate(user, "testEmail", "email.test.title");
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("test title");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isEqualTo("<html>test title, http://127.0.0.1:8080, john</html>\n");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendActivationEmail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendActivationEmail(user);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testCreationEmail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendCreationEmail(user);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendPasswordResetMail() throws Exception {
        User user = new User();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setLogin("john");
        user.setEmail("john.doe@example.com");
        mailService.sendPasswordResetMail(user);
        verify(javaMailSender).send((MimeMessage) messageCaptor.capture());
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendEmailWithException() throws Exception {
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
    }

    @Test
    public void sendOrderConfirmationMailToCustomer() throws Exception {
        User user = new User();
        user.setLangKey( Constants.DEFAULT_LANGUAGE );
        user.setLogin( "john" );
        user.setEmail( "john.doe@example.com" );

        Order order = new Order();
        Customer customer = new Customer();
        customer.setUser( user );
        order.setCustomer( customer );
        Product product = new Product();
        product.setName( "product-name" );
        product.setPrice( 123F );
        order.setProduct( product );
        order.setDeliveryType( DeliveryType.PICKUP );

        mailService.sendOrderCreationMails( order, user );
        verify( javaMailSender, times( 2 ) ).send( (MimeMessage) messageCaptor.capture() );
        MimeMessage message = (MimeMessage) messageCaptor.getValue();
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( "confirmation@mail.com" );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent().toString() ).isNotEmpty();
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

}
