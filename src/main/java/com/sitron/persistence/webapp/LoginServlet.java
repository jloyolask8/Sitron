/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sitron.persistence.webapp;

import com.auth0.RequestNonceStorage;
import com.auth0.NonceGenerator;
import com.auth0.NonceStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final NonceGenerator nonceGenerator = new NonceGenerator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NonceStorage nonceStorage = new RequestNonceStorage(request);
        System.out.println("request.getServletPath() = "+request.getServletPath());
        if (!"/favicon.ico".equals(request.getServletPath())) {
            String nonce = nonceGenerator.generateNonce();
            nonceStorage.setState(nonce);
            request.setAttribute("state", nonce);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}