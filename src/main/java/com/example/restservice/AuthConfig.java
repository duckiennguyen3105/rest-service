package com.example.restservice;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AuthConfig {
    private static final String clientId = "b0d61512-febc-4c54-9662-2e821f723b57";
    private static final String clientSecret = "B5O0UY8acL~JzEiP~_9PwL_kUVeimc-sx2";
    private static final String tenantId = "8154749f-7d9d-4c71-82a0-211b91ca5ba3";
    // endregion
    // region subscription information
    private final String publicUrl = "https://1a0d25a85463.ngrok.io"; // eg https://c2ddde53.ngrok.io no trailing slash
    private final String resource = "teams/allMessages"; // eg
    // teams/9c05f27f-f866-4cc0-b4c2-6225a4568bc5/channels/19:015c392a3030451f8b52fac6084be56d@thread.skype/messages
    private final String changeType = "created";
    // endregion
    // region certificate information
    private final String storename = "JKSkeystore.jks";
    private final String alias = "selfsignedjks";
    private final String storepass = "betahouse3105";
    // endregion

    private static final List<String> scopes = Arrays.asList("https://graph.microsoft.com/.default");
    private static final List<String> scopes2 = Arrays.asList("User.Read,Tasks.Read,Tasks.ReadWrite,Group.Read.All,Group.ReadWrite.All".split(","));
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private JwkKeyResolver jwksResolver;

    public static TokenCredentialAuthProvider authProvider = null;
    public static GraphServiceClient<Request> graphClient = null;

    public static GraphServiceClient<Request> getAdminPermission() {
        final ClientSecretCredential defaultCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();
        final IAuthenticationProvider authProvider = new TokenCredentialAuthProvider(scopes, defaultCredential);
        return GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
    }


    public static GraphServiceClient<Request> getClient() {


        //insert Token
        final DeviceCodeCredential credential = new DeviceCodeCredentialBuilder()
                .clientId(clientId)
                .challengeConsumer(challenge -> System.out.println(challenge.getMessage()))
                .build();

        authProvider = new TokenCredentialAuthProvider(scopes2, credential);

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.ERROR);

        // Build a Graph client
        return  GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .logger(logger)
                .buildClient();
    }

    public static String getUserAccessToken()
    {
        try {
            URL meUrl = new URL("https://graph.microsoft.com/v1.0/me");
            return authProvider.getAuthorizationTokenAsync(meUrl).get();
        } catch(Exception ex) {
            return null;
        }
    }
}
