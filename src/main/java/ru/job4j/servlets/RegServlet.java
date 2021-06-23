package ru.job4j.servlets;

import ru.job4j.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(value = "/reg")
public class RegServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("title", "Регистрация пользователя.");
        req.getRequestDispatcher("WEB-INF/user/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("users").include(req, resp);
        if (req.getAttribute("error") == null || ((String) req.getAttribute("error")).isEmpty()) {
            resp.sendRedirect("index");
        } else {
            doGet(req, resp);
        }
    }
}
