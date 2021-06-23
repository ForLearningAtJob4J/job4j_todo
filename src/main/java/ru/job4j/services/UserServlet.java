package ru.job4j.services;

import org.json.JSONArray;
import ru.job4j.model.User;
import ru.job4j.store.PostgreHbnStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(value = "/users/*")
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TaskServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new JSONArray(PostgreHbnStore.instOf().findAllUsers()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = new User()
                .setId(0)
                .setName(req.getParameter("name"))
                .setEmail(req.getParameter("email"))
                .setPassword(req.getParameter("password"));
        try {
            PostgreHbnStore.instOf().add(user);
            req.getSession().setAttribute("user", user);
        } catch (SQLException e) {
            req.setAttribute("error", e.getCause().getMessage());
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = new User()
                .setId(Integer.parseInt(req.getParameter("id")))
                .setName(req.getParameter("name"))
                .setEmail(req.getParameter("email"))
                .setPassword(req.getParameter("password"));

        try {
            PostgreHbnStore.instOf().update(user);
        } catch (SQLException e) {
            req.setAttribute("error", e.getMessage());
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        req.getSession().setAttribute("user", user);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            PostgreHbnStore.instOf().delete(new User().setId(Integer.parseInt(req.getParameter("id"))));
            req.getSession().setAttribute("user", null);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }
}