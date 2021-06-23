package ru.job4j.servlets;


import ru.job4j.model.User;
import ru.job4j.store.PostgreHbnStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/auth")
public class AuthServlet extends HttpServlet {
    @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("exit".equals(req.getParameter("op"))) {
            req.getSession().setAttribute("user", null);
        }
        req.getRequestDispatcher("WEB-INF/auth.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = PostgreHbnStore.instOf().findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("index");
        } else {
            req.setAttribute("error", "Неверный пароль, либо пользователь с таким email не зарегистрирован!");
            req.getRequestDispatcher("WEB-INF/auth.jsp").forward(req, resp);
        }
    }
}