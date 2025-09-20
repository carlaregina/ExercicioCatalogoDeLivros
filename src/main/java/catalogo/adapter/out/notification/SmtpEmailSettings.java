package catalogo.adapter.out.notification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SmtpEmailSettings {


    public final String host;
    public final int port;
    public final boolean auth;
    public final boolean tls;
    public final boolean ssl;
    public final String username;
    public final String password;
    public final String from;
    public final List<String> to;

    public SmtpEmailSettings(String host, int port, boolean auth, boolean tls, boolean ssl,
                             String username, String password, String from, List<String> to) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.tls = tls;
        this.ssl = ssl;
        this.username = username;
        this.password = password;
        this.from = from;
        this.to = to;
    }

    public static SmtpEmailSettings fromEnv() {
        Map<String, String> env = System.getenv();

        String host = env.getOrDefault("EMAIL_HOST", "");
        String portStr = env.getOrDefault("EMAIL_PORT", "587");
        int port = Integer.parseInt(portStr);

        boolean auth = Boolean.parseBoolean(env.getOrDefault("EMAIL_AUTH", "true"));
        boolean tls  = Boolean.parseBoolean(env.getOrDefault("EMAIL_TLS", "true"));
        boolean ssl  = Boolean.parseBoolean(env.getOrDefault("EMAIL_SSL", "false"));

        String user = env.getOrDefault("EMAIL_USER", "");
        String pass = env.getOrDefault("EMAIL_PASS", "");
        String from = env.getOrDefault("EMAIL_FROM", "");
        String toRaw = env.getOrDefault("EMAIL_TO", "");
        List<String> to = toRaw.isBlank() ? List.of() :
                Arrays.stream(toRaw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();

        return new SmtpEmailSettings(host, port, auth, tls, ssl, user, pass, from, to);
    }

    public boolean isConfigured() {
        return host != null && !host.isBlank()
                && from != null && !from.isBlank()
                && to != null && !to.isEmpty()

                && (!auth || (username != null && !username.isBlank() && password != null && !password.isBlank()));
    }

}
