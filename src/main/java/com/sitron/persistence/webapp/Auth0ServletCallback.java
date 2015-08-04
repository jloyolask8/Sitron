/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.webapp;

/**
 *
 * @author jonathan
 */
import com.auth0.Auth0User;
import com.auth0.NonceStorage;
import com.auth0.RequestNonceStorage;
import com.auth0.Tokens;
import com.sitron.persistence.entities.Usuario;
import static java.util.Arrays.asList;
import static us.monoid.web.Resty.content;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.Validate;

import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class Auth0ServletCallback extends HttpServlet {

    // Transaction is required to perform this operation in a servlet
    @Resource
    private UserTransaction utx = null;

    @PersistenceContext(unitName = "sitronPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    private Properties properties = new Properties();
    private String redirectOnSuccess;
    private String redirectOnFail;

    /**
     * Fetch properties to be used. User is encourage to override this method.
     *
     * Auth0 uses the ServletContext parameters:
     *
     * <dl>
     * <dt>auth0.client_id</dd>
     * <dd>Application client id</dd>
     * <dt>auth0.client_secret</dt>
     * <dd>Application client secret</dd>
     * <dt>auth0.domain</dt>
     * <dd>Auth0 domain</dd>
     * </dl>
     *
     * Auth0ServletCallback uses these ServletConfig parameters:
     *
     * <dl>
     * <dt>auth0.redirect_on_success</dt>
     * <dd>Where to send the user after successful login.</dd>
     * <dt>auth0.redirect_on_error</dt>
     * <dd>Where to send the user after failed login.</dd>
     * </dl>
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        redirectOnSuccess = readParameter("auth0.redirect_on_success", config);

        redirectOnFail = readParameter("auth0.redirect_on_error", config);

        for (String param : asList("auth0.client_id", "auth0.client_secret",
                "auth0.domain")) {
            properties.put(param, readParameter(param, config));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println("Auth0ServletCallback.doGet");

        if (hasError(req)) {
            onError(req, resp);
        } else {
            if (isValidRequest(req, resp)) {
                try {
                    Tokens tokens = fetchTokens(req);
                    Auth0User user = fetchUser(tokens);
                    boolean stored = store(tokens, user, req);
                    if (stored) {
                        System.out.println("USER SUCCESSFULLY SAVED TO DATABASE:" + user.getUserId());
                        onSuccess(req, resp);
                    } else {
                        throw new IllegalStateException("User was not saved to database:" + user.getUserId());
                    }

                } catch (IllegalArgumentException | IllegalStateException ex) {
                    System.out.println("Error:" + ex);
                    onFailure(req, resp, ex);
                }

            } else {
                System.out.println("invalid request!!");
            }
        }

    }

    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Redirect user to home
        resp.sendRedirect(req.getContextPath() + redirectOnSuccess);
    }

    protected void onError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.sendRedirect(req.getContextPath() + redirectOnFail + "?"
                + req.getQueryString());
    }

    protected void onFailure(HttpServletRequest req, HttpServletResponse resp,
            Exception ex) throws ServletException, IOException {
        ex.printStackTrace();
        resp.sendRedirect(req.getContextPath() + redirectOnFail + "?"
                + req.getQueryString());
    }

//	protected void store(Tokens tokens, Auth0User user, HttpServletRequest req) {
//		HttpSession session = req.getSession();
//
//		// Save tokens on a persistent session
//		session.setAttribute("auth0tokens", tokens);
//		session.setAttribute("user", user);
//	}
    /**
     * Stores the user in the DB if necessary!
     *
     * @param tokens
     * @param user
     * @param req
     */
    protected boolean store(Tokens tokens, Auth0User user, HttpServletRequest req) {

        try {

            System.out.println("user:" + user);
            final String userId = (String) user.getUserId();//We will use the email as the id for the user

            Usuario usuario = null;
            try {
                usuario = (Usuario) getEntityManager().createNamedQuery("Usuario.findByUserId").setParameter("userId", userId).getSingleResult();
            } catch (javax.persistence.NoResultException no) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Usuario not found for email/id: {0}", userId);
            }

            if (usuario == null) {
                System.out.println("new user");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                usuario = new Usuario();
                usuario.setCreationDate(sdf.format(new Date()));
                usuario.setIdUsuario(userId);
                setUserData(usuario, user);

                utx.begin();
                getEntityManager().persist(usuario);
                utx.commit();
            } else {
                System.out.println("existing user");
                // Override info?
                utx.begin();
                setUserData(usuario, user);
                getEntityManager().merge(usuario);
                utx.commit();
            }

            HttpSession session = req.getSession();

            // Save tokens on a persistent session
            session.setAttribute("auth0tokens", tokens);
            session.setAttribute("user", user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    private void setUserData(Usuario usuario, final Auth0User userData) {

        if (StringUtils.isEmpty(usuario.getNickname())) {
            usuario.setNickname(userData.getNickname());
        }
        if (StringUtils.isEmpty(usuario.getPicture())) {
            usuario.setPicture(userData.getPicture());
        }

        try {
            if (StringUtils.isEmpty(usuario.getEmail())) {
                usuario.setEmail(userData.getEmail());
            }
        } catch (Exception e) {
            System.out.println("email not found");
        }

        try {
            if (StringUtils.isEmpty(usuario.getGenre())) {
                final String emailv = (String) userData.getProperty("email_verified");
                final Boolean email_verified = Boolean.valueOf(emailv);
                usuario.setEmailVerified(email_verified);
            }
        } catch (Exception e) {
            System.out.println("email_verified not found");
        }

        try {
            if (StringUtils.isEmpty(usuario.getGenre())) {
                usuario.setGenre((String) userData.getProperty("gender"));
            }
        } catch (Exception e) {
            System.out.println("gender not found");
        }
        try {
            if (StringUtils.isEmpty(usuario.getApellidos())) {
                usuario.setApellidos((String) userData.getProperty("family_name"));
            }
        } catch (Exception e) {
            System.out.println("family_name not found");
        }
        try {
            if (StringUtils.isEmpty(usuario.getNombres())) {
                usuario.setNombres((String) userData.getProperty("given_name"));
            }
        } catch (Exception e) {
            System.out.println("given_name not found");
        }

        try {
            if (StringUtils.isEmpty(usuario.getLocale())) {
                usuario.setLocale((String) userData.getProperty("locale"));
            }
        } catch (Exception e) {
            System.out.println("locale not found");
        }

    }

    private Tokens fetchTokens(HttpServletRequest req) throws IOException {
        String authorizationCode = getAuthorizationCode(req);
        Resty resty = createResty();

        String tokenUri = getTokenUri();

        JSONObject json = new JSONObject();
        try {
            json.put("client_id", properties.get("auth0.client_id"));
            json.put("client_secret", properties.get("auth0.client_secret"));
            String requri = req.getRequestURL().toString();
            //ugly workaround for proxy conf, remove the port 
            requri = requri.replace(":8089", "");
            json.put("redirect_uri", requri);
            json.put("grant_type", "authorization_code");
            json.put("code", authorizationCode);
            
            System.out.println("* JSON:\n" + json);

            JSONResource tokenInfo = resty.json(tokenUri, content(json));
            return new Tokens(tokenInfo.toObject());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException("Cannot get Token from Auth0", ex);
        }
    }

    private Auth0User fetchUser(Tokens tokens) {
        Resty resty = createResty();

        String userInfoUri = getUserInfoUri(tokens.getAccessToken());

        try {
            JSONResource json = resty.json(userInfoUri);
            return new Auth0User(json.toObject());
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot get User from Auth0", ex);
        }
    }

    private String getTokenUri() {
        return getUri("/oauth/token");
    }

    private String getUserInfoUri(String accessToken) {
        return getUri("/userinfo?access_token=" + accessToken);
    }

    private String getUri(String path) {
        return String.format("https://%s%s", (String) properties.get("auth0.domain"), path);
    }

    private String getAuthorizationCode(HttpServletRequest req) {
        String code = req.getParameter("code");
        Validate.notNull(code);
        return code;
    }

    /**
     * Override this method to specify a different Resty client. For example, if
     * you want to add a proxy, this would be the place to set it
     *
     *
     * @return {@link Resty} that will be used to perform all requests to Auth0
     */
    protected Resty createResty() {
        return new Resty();
    }

    private boolean isValidRequest(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        if (hasError(req) || !isValidState(req)) {
            return false;
        }
        return true;
    }

    private boolean isValidState(HttpServletRequest req) {
        return req.getParameter("state")
                .equals(getNonceStorage(req).getState());
    }

    private static boolean hasError(HttpServletRequest req) {
        return req.getParameter("error") != null;
    }

    static String readParameter(String parameter, ServletConfig config) {
        String first = config.getInitParameter(parameter);
        if (hasValue(first)) {
            return first;
        }
        final String second = config.getServletContext().getInitParameter(
                parameter);
        if (hasValue(second)) {
            return second;
        }
        throw new IllegalArgumentException(parameter + " needs to be defined");
    }

    private static boolean hasValue(String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * Override this method to specify a different NonceStorage:
     *
     * <code>
     *     protected NonceStorage getNonceStorage(HttpServletRequest request) {
     *         return MyNonceStorageImpl(request);
     *     }
     * </code>
     */
    protected NonceStorage getNonceStorage(HttpServletRequest request) {
        return new RequestNonceStorage(request);
    }

}
