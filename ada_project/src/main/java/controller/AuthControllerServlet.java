package controller;

import bean.User;
import utils.DBUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthControllerServlet extends HttpServlet {
    final static String AUTHORIZED = "is_authorized";
    private final static String EMAIL_FIELD = "email";
    private final static String PASSWORD_FIELD = "password";
    final static String CURRENT_USER = "current_user";
    private final static String ERROR_MESSAGE = "error_message";
    static final String SUCCESS_MESSAGE = "success_message";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        System.out.println(session.getAttribute(AuthControllerServlet.AUTHORIZED));
        if (session.getAttribute(AuthControllerServlet.AUTHORIZED) != null) {
            if (req.getParameter("action") != null && req.getParameter("action").equals("logout")) {
                session.removeAttribute(AuthControllerServlet.AUTHORIZED);
                session.removeAttribute(AuthControllerServlet.CURRENT_USER);
                session.removeAttribute(DashboardControllerServlet.FIRST_NAME);
                session.removeAttribute(DashboardControllerServlet.LAST_NAME);
                session.removeAttribute(DashboardControllerServlet.AGE);
                session.removeAttribute(DashboardControllerServlet.CITY);
                session.removeAttribute(DashboardControllerServlet.COUNTRY);
            }
        }
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter(AuthControllerServlet.EMAIL_FIELD);
        String password = req.getParameter(AuthControllerServlet.PASSWORD_FIELD);
        System.out.println(email + ' ' + password);
        if (email != null && password != null && email.length() > 0 && password.length() > 0) {
            try {
                boolean userExists = DBUtils.authenticateWithCredentials(email, password);
                if (userExists) {
                    System.out.println("User found");
                    HttpSession session = req.getSession();
                    session.setAttribute(AuthControllerServlet.AUTHORIZED, true);
                    session.setAttribute(AuthControllerServlet.CURRENT_USER, email);
                    resp.sendRedirect("dashboard");
                } else {
                    System.out.println("User not found");
                    req.setAttribute(AuthControllerServlet.ERROR_MESSAGE, new String[]{"No such user exists in the system"});
                    getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
                }
            } catch (SQLException throwables) {
                req.setAttribute(AuthControllerServlet.ERROR_MESSAGE, new String[]{"Database connection error"});
                throwables.printStackTrace();
                getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        } else {
            System.out.println("empty fields");
            req.setAttribute(AuthControllerServlet.ERROR_MESSAGE, new String[]{"fill all fields"});
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
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
