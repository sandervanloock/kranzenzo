package be.sandervl.kranzenzo.service;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.config.ApplicationProperties;
import be.sandervl.kranzenzo.config.Constants;
import be.sandervl.kranzenzo.config.DummyS3Configuration;
import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.domain.ProductOrder;
import be.sandervl.kranzenzo.domain.User;
import be.sandervl.kranzenzo.domain.enumeration.DeliveryType;
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
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;

import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KranzenzoApp.class)
@Import(DummyS3Configuration.class)
public class MailServiceIntTest {

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor <MimeMessage> messageCaptor;

    private MailService mailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        doNothing().when( javaMailSender ).send( any( MimeMessage.class ) );
        mailService = new MailService( jHipsterProperties, applicationProperties, javaMailSender, messageSource, templateEngine );
    }

    @Test
    public void testSendEmail() throws Exception {
        mailService.sendEmail( "john.doe@example.com", "testSubject", "testContent", false, false );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getSubject() ).isEqualTo( "testSubject" );
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( "john.doe@example.com" );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent() ).isInstanceOf( String.class );
        assertThat( message.getContent().toString() ).isEqualTo( "testContent" );
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/plain; charset=UTF-8" );
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        mailService.sendEmail( "john.doe@example.com", "testSubject", "testContent", false, true );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getSubject() ).isEqualTo( "testSubject" );
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( "john.doe@example.com" );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent() ).isInstanceOf( String.class );
        assertThat( message.getContent().toString() ).isEqualTo( "testContent" );
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testSendMultipartEmail() throws Exception {
        mailService.sendEmail( "john.doe@example.com", "testSubject", "testContent", true, false );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart( 0 ).getContent()).getBodyPart( 0 );
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo( aos );
        assertThat( message.getSubject() ).isEqualTo( "testSubject" );
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( "john.doe@example.com" );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent() ).isInstanceOf( Multipart.class );
        assertThat( aos.toString() ).isEqualTo( "\r\ntestContent" );
        assertThat( part.getDataHandler().getContentType() ).isEqualTo( "text/plain; charset=UTF-8" );
    }

    @Test
    public void testSendMultipartHtmlEmail() throws Exception {
        mailService.sendEmail( "john.doe@example.com", "testSubject", "testContent", true, true );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart( 0 ).getContent()).getBodyPart( 0 );
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo( aos );
        assertThat( message.getSubject() ).isEqualTo( "testSubject" );
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( "john.doe@example.com" );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent() ).isInstanceOf( Multipart.class );
        assertThat( aos.toString() ).isEqualTo( "\r\ntestContent" );
        assertThat( part.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testSendEmailFromTemplate() throws Exception {
        User user = new User();
        user.setLogin( "john" );
        user.setEmail( "john.doe@example.com" );
        user.setLangKey( "en" );
        mailService.sendEmailFromTemplate( user, "mail/testEmail", "email.test.title" );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getSubject() ).isEqualTo( "test title" );
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( user.getEmail() );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent().toString() )
            .isEqualToNormalizingNewlines( "<html>test title, http://127.0.0.1:8080, john</html>\n" );
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testSendActivationEmail() throws Exception {
        User user = new User();
        user.setLangKey( Constants.DEFAULT_LANGUAGE );
        user.setLogin( "john" );
        user.setEmail( "john.doe@example.com" );
        mailService.sendActivationEmail( user );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( user.getEmail() );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent().toString() ).isNotEmpty();
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testCreationEmail() throws Exception {
        User user = new User();
        user.setLangKey( Constants.DEFAULT_LANGUAGE );
        user.setLogin( "john" );
        user.setEmail( "john.doe@example.com" );
        mailService.sendCreationEmail( user );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( user.getEmail() );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent().toString() ).isNotEmpty();
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testSendPasswordResetMail() throws Exception {
        User user = new User();
        user.setLangKey( Constants.DEFAULT_LANGUAGE );
        user.setLogin( "john" );
        user.setEmail( "john.doe@example.com" );
        mailService.sendPasswordResetMail( user );
        verify( javaMailSender ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertThat( message.getAllRecipients()[0].toString() ).isEqualTo( user.getEmail() );
        assertThat( message.getFrom()[0].toString() ).isEqualTo( "test@localhost" );
        assertThat( message.getContent().toString() ).isNotEmpty();
        assertThat( message.getDataHandler().getContentType() ).isEqualTo( "text/html;charset=UTF-8" );
    }

    @Test
    public void testSendEmailWithException() throws Exception {
        doThrow( MailSendException.class ).when( javaMailSender ).send( any( MimeMessage.class ) );
        mailService.sendEmail( "john.doe@example.com", "testSubject", "testContent", false, false );
    }

    @Test
    public void testOrderConfirmationMails() {
        ProductOrder order = new ProductOrder();
        Product product = new Product();
        product.setName( "test-product" );
        product.setPrice( 4F );
        order.setProduct( product );
        order.setId( 123L );
        order.setDeliveryPrice( 45F );
        order.setIncludeBatteries( false );
        order.deliveryType( DeliveryType.PICKUP );
        User user = new User();
        user.setEmail( "svl@fe.com" );
        mailService.sendOrderCreationMails( order, user );
        verify( javaMailSender,times(2) ).send( messageCaptor.capture() );
        MimeMessage message = messageCaptor.getValue();
        assertNotNull( message );
    }
}
