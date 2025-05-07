package com.nando.service;

import com.nando.exception.ErrorReadEmailException;
import com.nando.exception.NoEmailFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.MimeUtility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@ApplicationScoped
public class GmailService implements EmailService {

    private final String host = "pop.gmail.com";
    private final String username = "maltarecebimentocode@gmail.com";
    private final String password = "nsdg eklp mhuk ilhg";

    private final Properties properties;

    public GmailService() {
        properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.pop3.ssl.enable", "true");
    }

    @Override
    public String getBodyLastEmail() {
        try {
            Thread.sleep(10000);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3s");
            store.connect(host, username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            if (messages.length == 0) {
                throw new NoEmailFoundException();
            }

            Message lastMessage = messages[messages.length - 1];
            Object content = lastMessage.getContent();

            String body;
            if (content instanceof String) {
                body = decodeQuotedPrintable((String) content);
            } else if (content instanceof InputStream) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(MimeUtility.decode((InputStream) content, "quoted-printable"), StandardCharsets.UTF_8))) {
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    body = builder.toString();
                }
            } else {
                throw new RuntimeException("Conteúdo body não suportado");
            }

            emailFolder.close(false);
            store.close();

            return body;

        } catch (Exception e) {
            throw new ErrorReadEmailException(e);
        }
    }

    private String decodeQuotedPrintable(String encoded) {
        try (
                InputStream is = new ByteArrayInputStream(encoded.getBytes(StandardCharsets.UTF_8));
                InputStream qpStream = MimeUtility.decode(is, "quoted-printable");
                BufferedReader reader = new BufferedReader(new InputStreamReader(qpStream, StandardCharsets.UTF_8))
        ) {
            StringBuilder decoded = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                decoded.append(line).append("\n");
            }
            return decoded.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return encoded; // fallback: retorna original se falhar
        }
    }
}
