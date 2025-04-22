package com.cosium.matrix_communication_client;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Réda Housni Alaoui
 */
public class MatrixResourcesFactory {

  public HttpsBuilder builder() {
    return new MatrixResourcesBuilder();
  }

  public interface HttpsBuilder {
    HostnameBuilder https(boolean https);

    default HostnameBuilder https() {
      return https(true);
    }

    default HostnameBuilder http() {
      return https(false);
    }

    AuthenticationBuilder uri(String serverUri);
  }

  public interface HostnameBuilder {
    PortBuilder hostname(String hostname);
  }

  public interface PortBuilder {

    AuthenticationBuilder port(Integer port);

    default AuthenticationBuilder defaultPort() {
      return port(null);
    }
  }

  public interface AuthenticationBuilder {
    FinalBuilder accessToken(String accessToken);

    FinalBuilder usernamePassword(String username, String password);
  }

  public interface FinalBuilder {

    FinalBuilder connectTimeout(Duration connectTimeout);

    MatrixResources build();
  }

  private static class MatrixResourcesBuilder
      implements HttpsBuilder, HostnameBuilder, PortBuilder, AuthenticationBuilder, FinalBuilder {

    private boolean https = true;
    private String hostname;
    private Integer port;
    private Duration connectTimeout = Duration.of(30, ChronoUnit.SECONDS);
    private AccessTokenFactoryFactory accessTokenFactoryFactory;

    @Override
    public AuthenticationBuilder uri(String serverUri) {
      try {
        URI uri = new URI(serverUri);
        String protocol = uri.getScheme();

        // Set the protocol
        if ("https".equalsIgnoreCase(protocol)) {
          this.https = true;
        } else if ("http".equalsIgnoreCase(protocol)) {
          this.https = false;
        } else {
          throw new IllegalArgumentException("URI must have http or https protocol: " + serverUri);
        }

        // Set the hostname
        this.hostname = uri.getHost();
        if (this.hostname == null || this.hostname.isEmpty()) {
          throw new IllegalArgumentException("URI must have a host: " + serverUri);
        }

        // Set the port
        this.port = uri.getPort() != -1 ? uri.getPort() : null;

        return this;
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException("Invalid URI: " + serverUri, e);
      }
    }

    @Override
    public MatrixResourcesBuilder https(boolean https) {
      this.https = https;
      return this;
    }

    @Override
    public MatrixResourcesBuilder hostname(String hostname) {
      this.hostname = hostname;
      return this;
    }

    @Override
    public MatrixResourcesBuilder port(Integer port) {
      this.port = port;
      return this;
    }

    @Override
    public MatrixResourcesBuilder accessToken(String accessToken) {
      accessTokenFactoryFactory = (h, j, u) -> () -> accessToken;
      return this;
    }

    @Override
    public FinalBuilder usernamePassword(String username, String password) {
      accessTokenFactoryFactory = new UsernamePasswordAccessTokenFactoryFactory(username, password);
      return this;
    }

    @Override
    public FinalBuilder connectTimeout(Duration connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public MatrixResources build() {
      return new SimpleMatrixResources(
          https, hostname, port, connectTimeout, accessTokenFactoryFactory);
    }
  }
}
