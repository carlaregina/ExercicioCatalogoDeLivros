package catalogo.adapter.out.notification;

import catalogo.domain.model.Livro;
import catalogo.domain.ports.NotificadorDeLivroCadastrado;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class ConsoleNotificadorDeLivroCadastrado implements NotificadorDeLivroCadastrado {

    private SmtpEmailSettings cfg;


    public ConsoleNotificadorDeLivroCadastrado() {
    }


    public ConsoleNotificadorDeLivroCadastrado(SmtpEmailSettings cfg) {
        this.cfg = cfg;
    }

    public void SmtpNotificadorDeLivroCadastrado(SmtpEmailSettings cfg) {
        this.cfg = cfg;
    }

    @Override
    public void notificar(Livro livro) {
        try {
            Session session = Session.getInstance(criarPropriedades(), autenticador());
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(cfg.from));
            InternetAddress[] recipients = cfg.to.stream()
                    .map(this::internetAddressSafe)
                    .toArray(InternetAddress[]::new);
            message.setRecipients(Message.RecipientType.TO, recipients);

            message.setSubject("Novo livro cadastrado: " + livro.getTitulo());
            String corpo = """
                    Um novo livro foi cadastrado.

                    Título: %s
                    Autor: %s
                    Ano: %d
                    ID: %s

                    -- Catálogo
                    """.formatted(livro.getTitulo(), livro.getAutor(), livro.getAnoPublicacao(), livro.getId());
            message.setText(corpo);

            Transport.send(message);
            System.out.println("[EMAIL] Enviado com sucesso para: " + cfg.to);
        } catch (Exception e) {

            System.err.println("[EMAIL] Falha ao enviar notificação: " + e.getMessage());
        }
    }

    private Properties criarPropriedades() {
        Properties props = new Properties();
        props.put("mail.smtp.host", cfg.host);
        props.put("mail.smtp.port", String.valueOf(cfg.port));
        props.put("mail.smtp.auth", String.valueOf(cfg.auth));


        props.put("mail.smtp.starttls.enable", String.valueOf(cfg.tls));


        if (cfg.ssl) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        return props;
    }

    private Authenticator autenticador() {
        if (!cfg.auth) return null;
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cfg.username, cfg.password);
            }
        };
    }

    private InternetAddress internetAddressSafe(String email) {
        try {
            return new InternetAddress(email, true);
        } catch (Exception e) {
            throw new RuntimeException("E-mail de destino inválido: " + email);
        }
    }

}
