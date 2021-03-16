package controller;

import bean.User;
import utils.DBUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DashboardControllerServlet extends HttpServlet {
    final static String FIRST_NAME = "first_name";
    final static String LAST_NAME = "last_name";
    final static String AGE = "age";
    final static String CITY = "city";
    final static String COUNTRY = "country";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("here (GET)");
        HttpSession session = req.getSession();
        User user = null;
        List<String> registeredCourses;
        List<String> availableCourses = null;
        String email = (String) session.getAttribute(AuthControllerServlet.CURRENT_USER);

        if (session.getAttribute(AuthControllerServlet.AUTHORIZED) != null
                && (boolean) session.getAttribute(AuthControllerServlet.AUTHORIZED)) {
            try {
                user = DBUtils.getUserDataWithEmail(email);
                if (req.getParameter("action") != null) {
                    System.out.println(req.getParameter("action"));
                }
                if (req.getParameter("action") != null) {
                    if (req.getParameter("action").startsWith("drop")) {
                        String course = (req.getParameter("action").split("/")[1]);
                        System.out.println(user.getCourses());
                        try {
                            System.out.println("now here");
                            DBUtils.deleteCourse(email, course);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else if (req.getParameter("action").startsWith("register")) {
                        String course = (req.getParameter("action").split("/")[1]);
                        System.out.println(course);
                        try {
                            System.out.println("registering");
                            DBUtils.registerForCourse(email, course);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }

                registeredCourses = DBUtils.getRegisteredCoursesWithEmail(email);
                if (registeredCourses != null)
                    user.setCourses(new HashSet<>(registeredCourses));
                else
                    user.setCourses(new HashSet<>());

                availableCourses = DBUtils.getAvailableCourses(email);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            req.setAttribute("availableCourses", availableCourses);
            req.setAttribute("user", user);
            System.out.println("i am here");
            getServletContext().getRequestDispatcher("/dashboard.jsp").forward(req, resp);
        } else {
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request is in Dashboard Controller (POST)");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute(AuthControllerServlet.CURRENT_USER);
        String firstName = req.getParameter(DashboardControllerServlet.FIRST_NAME);
        String lastName = req.getParameter(DashboardControllerServlet.LAST_NAME);
        String age = req.getParameter(DashboardControllerServlet.AGE);
        String city = req.getParameter(DashboardControllerServlet.CITY);
        String country = req.getParameter(DashboardControllerServlet.COUNTRY);

        List<String> details = Arrays.asList(firstName, lastName, age, city, country);
        details.stream()
                .filter(item -> item != null && item.length() > 0)
                .forEach(item -> {
                    try {
                        if (item.equals(firstName)) {
                            DBUtils.updateFirstNameWithEmail(firstName, email);

                        }
                        if (item.equals(lastName)) {
                            DBUtils.updateLastNameWithEmail(lastName, email);
                        }
                        if (item.equals(age)) {
                            DBUtils.updateAgeWithEmail(age, email);
                        }
                        if (item.equals(city)) {
                            DBUtils.updateCityWithEmail(city, email);
                        }
                        if (item.equals(country)) {
                            DBUtils.updateCountryWithEmail(country, email);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
        resp.sendRedirect("dashboard");
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
