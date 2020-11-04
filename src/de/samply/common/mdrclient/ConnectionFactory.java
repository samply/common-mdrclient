
package de.samply.common.mdrclient;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.core.util.Base64;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP connection factory with proxy settings.
 */
public class ConnectionFactory implements HttpURLConnectionFactory {

  /**
   * The proxy settings.
   */
  private Proxy proxy;

  /**
   * Proxy host name.
   */
  private String proxyHost;

  /**
   * Proxy port.
   */
  private Integer proxyPort;

  /**
   * Proxy realm name.
   */
  private String proxyRealm;

  /**
   * Proxy username.
   */
  private String username;

  /**
   * Proxy password.
   */
  private String password;

  /**
   * Logger instance for this class.
   */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Creates an instance.
   */
  public ConnectionFactory() {
  }

  /**
   * Creates an instance with the specified proxy settings.
   *
   * @param proxyHost the proxy host name
   * @param proxyPort the proxy port
   */
  public ConnectionFactory(final String proxyHost, final Integer proxyPort) {
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
  }

  /**
   * Get the proxy host.
   *
   * @return the proxy host
   */
  public final String getProxyHost() {
    return proxyHost;
  }

  /**
   * Set the proxy host.
   *
   * @param proxyHost the proxy host
   */
  public final void setProxyHost(final String proxyHost) {
    this.proxyHost = proxyHost;
  }


  /**
   * Get the proxy port.
   *
   * @return the proxy port
   */
  public final Integer getProxyPort() {
    return proxyPort;
  }

  /**
   * Set the proxy port.
   *
   * @param proxyPort the proxy port
   */
  public final void setProxyPort(final String proxyPort) {
    if (proxyPort == null) {
      this.proxyPort = null;
      return;
    }

    Integer port = null;
    try {
      port = Integer.parseInt(proxyPort);
    } catch (NumberFormatException e) {
      port = null;
    }

    this.proxyPort = port;
  }

  /**
   * Get the proxy realm name.
   *
   * @return the proxy realm name
   */
  public final String getProxyRealm() {
    return proxyRealm;
  }

  /**
   * Set the proxy realm name.
   *
   * @param proxyRealm the proxy realm name
   */
  public final void setProxyRealm(final String proxyRealm) {
    this.proxyRealm = proxyRealm;
  }

  /**
   * Get the proxy connection user name.
   *
   * @return the proxy connection user name
   */
  public final String getUsername() {
    return username;
  }

  /**
   * Set the proxy connection user name.
   *
   * @param username the proxy connection user name
   */
  public final void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Get the proxy connection password.
   *
   * @return password the proxy connection password
   */
  public final String getPassword() {
    return password;
  }

  /**
   * Set the proxy connection password.
   *
   * @param password the proxy connection password
   */
  public final void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public final HttpURLConnection getHttpURLConnection(final URL url) throws IOException {
    String protocol = url.getProtocol();
    String encoded = null;

    if (this.proxyPort != null && protocol.equals("http")) {
      logger.debug("getting HTTP connection with proxystuff " + proxyHost + ":" + proxyPort);
      initializeProxy();
      if (username != null) {
        encoded = injectProxyLogin(username, password);
      }
    }

    HttpURLConnection con;

    if (proxy != null) {
      con = (HttpURLConnection) url.openConnection(proxy);

      if (encoded != null) {
        logger.debug("setting Proxy-Authorization: Basic " + encoded);
        con.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
        if (proxyRealm != null && proxyRealm.length() > 0) {
          logger.debug("setting Proxy-Authenticate: Basic realm=" + proxyRealm);
          con.setRequestProperty("Proxy-Authenticate", "Basic realm=\"" + proxyRealm + "\"");
        }
      }
    } else {
      con = (HttpURLConnection) url.openConnection();
    }

    return con;
  }

  /**
   * Initialize the proxy with the provided host and port settings.
   */
  private void initializeProxy() {
    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
  }

  /**
   * Inject the proxy authentication settings.
   *
   * @param username the proxy username
   * @param password the proxy password
   * @return the username and password String representation.
   */
  private String injectProxyLogin(final String username, final String password) {
    Authenticator.setDefault(new Authenticator() {
      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
      }
    });

    return new String(Base64.encode(new String(username + ":" + password).getBytes()));
  }
}
