<%-- 
    Document   : login
    Created on : May 4, 2015, 3:50:26 PM
    Author     : jonathan
--%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <title>Login</title>
        <script src="https://cdn.auth0.com/js/lock-7.1.min.js"></script>
    </head>
    <body>

        <div align="center">
            <a class="navbar-brand" href="index.xhtml">
                <img class="logo-default" src="<%=request.getContextPath()%>/resources/assets/admin/layout5/img/logo.png" alt="Logo"/>
            </a>
        </div>

        <div id="root" style="width: 280px; margin: 40px auto;">
            embeded area
        </div>

        <script type="text/javascript">

            <%!
             // Converts a relative path into a full path
                // Taken from http://stackoverflow.com/posts/5212336/revisions
                public String buildUrl(HttpServletRequest request, String relativePath) {

                    try {
                        String scheme = request.getScheme();        // http
                        String serverName = request.getServerName();    // hostname.com
//                     int serverPort = request.getServerPort();    // 80
                        String contextPath = request.getContextPath();   // /mywebapp

                        // Reconstruct original requesting URL
                        StringBuffer url = new StringBuffer();
                        url.append(scheme).append("://").append(serverName);

                     //if ((serverPort != 80) && (serverPort != 443)) {
                        //url.append(":").append(serverPort);
                        //}
                        url.append(contextPath).append(relativePath);

                        return url.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            %>

            //      var widget = new Auth0Widget({
            //        domain:         '<%= application.getInitParameter("auth0.domain")%>',
            //        clientID:       '<%= application.getInitParameter("auth0.client_id")%>',
            //        callbackURL:    '<%= buildUrl(request, "/callback")%>'
            //      });


            var lock = new Auth0Lock('<%= application.getInitParameter("auth0.client_id")%>', '<%= application.getInitParameter("auth0.domain")%>');


            lock.show({
                container: 'root',
                callbackURL: '<%= buildUrl(request, "/callback")%>',
                responseType: 'code',
                dict: 'es',
                loginAfterSignup: false,
                rememberLastLogin: false,
                authParams: {
                    scope: 'openid profile'
                    , state: '${state}'
                }
            });

        </script>
        <% if (request.getParameter("error") != null) { %>
        <%-- TODO Escape and encode ${param.error} properly. It can be done using jstl c:out. --%>
        <div align="center">
            <span style="color: red;">${param.error}</span>

        </div>
        <% }%>

    </body>
</html>