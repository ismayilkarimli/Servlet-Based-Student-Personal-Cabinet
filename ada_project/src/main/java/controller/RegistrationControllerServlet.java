package controller;

import utils.DBUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationControllerServlet extends HttpServlet {
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL_FIELD = "email";
    private static final String ERROR_MESSAGE = "error_message";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Entered Registration controller (GET)");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Entered Registration controller (POST)");
        String email = req.getParameter(RegistrationControllerServlet.EMAIL_FIELD);
        String password = req.getParameter(RegistrationControllerServlet.PASSWORD_FIELD);

        if (email != null && password != null && email.length() > 0 && password.length() > 0) {
            try {
                boolean userExists = DBUtils.checkIfUserExists(email);
                if (!userExists) {
                    DBUtils.registerWithCredentials(email, password);
                    System.out.println("added successfully");
                    req.setAttribute(AuthControllerServlet.SUCCESS_MESSAGE, "successfully registered");
                    getServletContext().getRequestDispatcher("/auth.jsp").forward(req, resp);
                } else {
                    System.out.println("user exists");
                    req.setAttribute(RegistrationControllerServlet.ERROR_MESSAGE, "user with such email already exists");
                    getServletContext().getRequestDispatcher("/register.jsp").forward(req, resp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("empty fields");
            req.setAttribute(RegistrationControllerServlet.ERROR_MESSAGE, "fill all fields");
            getServletContext().getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
