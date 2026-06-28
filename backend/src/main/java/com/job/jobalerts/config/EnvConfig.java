package com.job.jobalerts.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment configuration loader for .env file
 * This loads environment variables before Spring Boot starts
 */
public class EnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static Dotenv dotenv;

    static {
        try {
            // Load .env file from project root
            dotenv = Dotenv.configure()
                    .ignoreIfMissing()  // Don't fail if .env is missing (for production)
                    .load();
            System.out.println("✅ .env file loaded successfully");
        } catch (Exception e) {
            System.out.println("⚠️ No .env file found, using system environment variables");
            dotenv = null;
        }
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        Map<String, Object> envProperties = new HashMap<>();

        if (dotenv != null) {
            // Load database configuration
            loadDatabaseProperties(envProperties);

            // Load Tap Academy configuration
            loadTapAcademyProperties(envProperties);

            // Load Brevo SMTP configuration (instead of Gmail)
            loadBrevoMailProperties(envProperties);

            // Load Scraper configuration
            loadScraperProperties(envProperties);

            // Load general application properties
            loadGeneralProperties(envProperties);
        }

        // Also check System environment variables as fallback
        loadSystemEnvironmentVariables(envProperties);

        // Add properties with highest priority
        propertySources.addFirst(new MapPropertySource("dotenvProperties", envProperties));

        System.out.println("✅ Environment properties loaded");
        printLoadedConfiguration(envProperties);
    }

    private void loadDatabaseProperties(Map<String, Object> props) {
        // Try DATABASE_URL first (NeonDB format)
        String dbUrl = dotenv.get("DATABASE_URL");
        if (dbUrl != null && !dbUrl.isEmpty()) {
            // Convert postgresql:// to jdbc:postgresql://
            String jdbcUrl = dbUrl.replace("postgresql://", "jdbc:postgresql://");
            props.put("spring.datasource.url", jdbcUrl);
            System.out.println("✅ Database URL configured from DATABASE_URL");
        }

        // Individual database properties (override if present)
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        if (host != null && port != null && dbName != null) {
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, dbName);
            props.put("spring.datasource.url", jdbcUrl);
            System.out.println("✅ Database URL configured from individual properties");
        }

        if (username != null) {
            props.put("spring.datasource.username", username);
        }

        if (password != null) {
            props.put("spring.datasource.password", password);
        }

        // HikariCP specific settings (optional from .env)
        String poolSize = dotenv.get("DB_POOL_SIZE");
        if (poolSize != null) {
            props.put("spring.datasource.hikari.maximum-pool-size", poolSize);
        }

        String connectionTimeout = dotenv.get("DB_CONNECTION_TIMEOUT");
        if (connectionTimeout != null) {
            props.put("spring.datasource.hikari.connection-timeout", connectionTimeout);
        }

        String maxLifetime = dotenv.get("DB_MAX_LIFETIME");
        if (maxLifetime != null) {
            props.put("spring.datasource.hikari.max-lifetime", maxLifetime);
        }

        String idleTimeout = dotenv.get("DB_IDLE_TIMEOUT");
        if (idleTimeout != null) {
            props.put("spring.datasource.hikari.idle-timeout", idleTimeout);
        }
    }

    private void loadTapAcademyProperties(Map<String, Object> props) {
        String tapEmail = dotenv.get("TAP_ACADEMY_EMAIL");
        String tapPassword = dotenv.get("TAP_ACADEMY_PASSWORD");
        String tapBaseUrl = dotenv.get("TAP_ACADEMY_URL");
        String tapJobLimit = dotenv.get("TAP_ACADEMY_JOB_LIMIT");

        if (tapEmail != null) {
            props.put("tap.academy.email", tapEmail);
        }

        if (tapPassword != null) {
            props.put("tap.academy.password", tapPassword);
        }

        if (tapBaseUrl != null) {
            props.put("tap.academy.url", tapBaseUrl);
        }

        if (tapJobLimit != null) {
            props.put("tap.academy.job.limit", tapJobLimit);
        }
    }

    // Load Brevo SMTP properties (updated from Gmail)
    private void loadBrevoMailProperties(Map<String, Object> props) {
        // Brevo SMTP Configuration
        String brevoUsername = dotenv.get("BREVO_USERNAME");
        String brevoSmtpKey = dotenv.get("BREVO_SMTP_KEY");

        if (brevoUsername != null && !brevoUsername.isEmpty()) {
            props.put("spring.mail.host", "smtp-brevo.com");
            props.put("spring.mail.username", brevoUsername);
            System.out.println("✅ Brevo Mail Username: " + brevoUsername);
        } else {
            // Fallback to old Gmail config if Brevo not set
            String mailHost = dotenv.get("MAIL_HOST");
            if (mailHost != null && !mailHost.isEmpty()) {
                props.put("spring.mail.host", mailHost);
            }

            String mailUsername = dotenv.get("MAIL_USERNAME");
            if (mailUsername != null && !mailUsername.isEmpty()) {
                props.put("spring.mail.username", mailUsername);
            }
        }

        if (brevoSmtpKey != null && !brevoSmtpKey.isEmpty()) {
            props.put("spring.mail.password", brevoSmtpKey);
            System.out.println("✅ Brevo SMTP Key loaded (length: " + brevoSmtpKey.length() + ")");
        } else {
            // Fallback to old Gmail config
            String mailPassword = dotenv.get("MAIL_PASSWORD");
            if (mailPassword != null && !mailPassword.isEmpty()) {
                props.put("spring.mail.password", mailPassword);
            }
        }

        // SMTP Port - Brevo uses 587
        String mailPort = dotenv.get("MAIL_PORT");
        if (mailPort != null && !mailPort.isEmpty()) {
            props.put("spring.mail.port", mailPort);
        } else {
            props.put("spring.mail.port", "587"); // Default Brevo port
        }

        // Mail properties
        props.put("spring.mail.properties.mail.smtp.auth", "true");
        props.put("spring.mail.properties.mail.smtp.starttls.enable", "true");
        props.put("spring.mail.properties.mail.smtp.starttls.required", "true");
        props.put("spring.mail.properties.mail.smtp.connectiontimeout", "5000");
        props.put("spring.mail.properties.mail.smtp.timeout", "5000");
        props.put("spring.mail.properties.mail.smtp.writetimeout", "5000");

        // Enable debug for troubleshooting
        String debugMail = dotenv.get("MAIL_DEBUG");
        if (debugMail != null && debugMail.equalsIgnoreCase("true")) {
            props.put("spring.mail.properties.mail.debug", "true");
        }

        // Set the from email (using BREVO_USERNAME as from email)
        if (brevoUsername != null && !brevoUsername.isEmpty()) {
            props.put("BREVO_USERNAME", brevoUsername);
            // Also set mail.from.email for EmailService
            props.put("mail.from.email", brevoUsername);
        }
    }

    private void loadScraperProperties(Map<String, Object> props) {
        String scraperEnabled = dotenv.get("SCRAPER_ENABLED");
        if (scraperEnabled != null) {
            props.put("SCRAPER_ENABLED", scraperEnabled);
        }

        String scraperCron = dotenv.get("SCRAPER_CRON_EXPRESSION");
        if (scraperCron != null) {
            props.put("SCRAPER_CRON_EXPRESSION", scraperCron);
        }

        String scraperSendEmails = dotenv.get("SCRAPER_SEND_EMAILS");
        if (scraperSendEmails != null) {
            props.put("SCRAPER_SEND_EMAILS", scraperSendEmails);
        }

        String scraperLogging = dotenv.get("SCRAPER_LOGGING_ENABLED");
        if (scraperLogging != null) {
            props.put("SCRAPER_LOGGING_ENABLED", scraperLogging);
        }
    }

    private void loadGeneralProperties(Map<String, Object> props) {
        // Server configuration
        String serverPort = dotenv.get("SERVER_PORT");
        if (serverPort != null) {
            props.put("server.port", serverPort);
        }

        String contextPath = dotenv.get("SERVER_CONTEXT_PATH");
        if (contextPath != null) {
            props.put("server.servlet.context-path", contextPath);
        }

        // JPA configuration
        String ddlAuto = dotenv.get("JPA_DDL_AUTO");
        if (ddlAuto != null) {
            props.put("spring.jpa.hibernate.ddl-auto", ddlAuto);
        }

        String showSql = dotenv.get("JPA_SHOW_SQL");
        if (showSql != null) {
            props.put("spring.jpa.show-sql", showSql);
            props.put("spring.jpa.properties.hibernate.show_sql", showSql);
        }

        String formatSql = dotenv.get("JPA_FORMAT_SQL");
        if (formatSql != null) {
            props.put("spring.jpa.properties.hibernate.format_sql", formatSql);
        }

        // Logging level
        String logLevel = dotenv.get("LOG_LEVEL_COM_JOB_JOBALERTS");
        if (logLevel != null) {
            props.put("logging.level.com.job.jobalerts", logLevel);
        }
    }

    private void loadSystemEnvironmentVariables(Map<String, Object> props) {
        // Fallback to system environment variables if .env doesn't have them
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && !props.containsKey("spring.datasource.url")) {
            String jdbcUrl = dbUrl.replace("postgresql://", "jdbc:postgresql://");
            props.put("spring.datasource.url", jdbcUrl);
        }

        String dbUsername = System.getenv("DB_USERNAME");
        if (dbUsername != null && !props.containsKey("spring.datasource.username")) {
            props.put("spring.datasource.username", dbUsername);
        }

        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword != null && !props.containsKey("spring.datasource.password")) {
            props.put("spring.datasource.password", dbPassword);
        }

        // Brevo fallback
        String brevoUsername = System.getenv("BREVO_USERNAME");
        if (brevoUsername != null && !props.containsKey("spring.mail.username")) {
            props.put("spring.mail.username", brevoUsername);
            props.put("BREVO_USERNAME", brevoUsername);
        }

        String brevoKey = System.getenv("BREVO_SMTP_KEY");
        if (brevoKey != null && !props.containsKey("spring.mail.password")) {
            props.put("spring.mail.password", brevoKey);
        }
    }

    private void printLoadedConfiguration(Map<String, Object> props) {
        System.out.println("\n📋 Loaded Configuration:");
        System.out.println("=========================");

        // Print database config (mask password)
        if (props.containsKey("spring.datasource.url")) {
            String url = (String) props.get("spring.datasource.url");
            String maskedUrl = url.replaceAll(":[^:@]+@", ":****@");
            System.out.println("🔗 Database URL: " + maskedUrl);
        }

        if (props.containsKey("spring.datasource.username")) {
            System.out.println("👤 Database Username: " + props.get("spring.datasource.username"));
        }

        // Print Tap Academy config
        if (props.containsKey("tap.academy.email")) {
            System.out.println("📧 Tap Academy Email: " + props.get("tap.academy.email"));
        }

        if (props.containsKey("tap.academy.url")) {
            System.out.println("🌐 Tap Academy URL: " + props.get("tap.academy.url"));
        }

        // Print Mail config (Brevo)
        if (props.containsKey("spring.mail.host")) {
            System.out.println("📧 Mail Host: " + props.get("spring.mail.host"));
        }

        if (props.containsKey("spring.mail.username")) {
            String username = (String) props.get("spring.mail.username");
            System.out.println("📧 Mail Username: " + username);
        }

        if (props.containsKey("spring.mail.password")) {
            String password = (String) props.get("spring.mail.password");
            String maskedPassword = password.substring(0, Math.min(4, password.length())) + "****";
            System.out.println("📧 Mail Password: " + maskedPassword);
        }

        // Scraper config
        if (props.containsKey("SCRAPER_ENABLED")) {
            System.out.println("🔄 Scraper Enabled: " + props.get("SCRAPER_ENABLED"));
        }

        if (props.containsKey("SCRAPER_CRON_EXPRESSION")) {
            System.out.println("⏰ Scraper Cron: " + props.get("SCRAPER_CRON_EXPRESSION"));
        }

        // Server config
        if (props.containsKey("server.port")) {
            System.out.println("🚀 Server Port: " + props.get("server.port"));
        }

        System.out.println("=========================\n");
    }

    /**
     * Helper method to get a property from .env directly
     * Useful for non-Spring components
     */
    public static String getProperty(String key) {
        if (dotenv != null) {
            return dotenv.get(key);
        }
        return System.getenv(key);
    }

    /**
     * Helper method to get a property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        if (dotenv != null) {
            String value = dotenv.get(key);
            return value != null ? value : defaultValue;
        }
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
}