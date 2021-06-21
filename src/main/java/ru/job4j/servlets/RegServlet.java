package ru.job4j.servlets;

import ru.job4j.model.User;
import ru.job4j.store.PostgreHbnStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(value = "/reg.do")
public class RegServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("op") == null || "edit".equals(req.getParameter("op"))) {
            String id = req.getParameter("id");
            int intId;
            String title;
            if (id == null) {
                title = "Регистрация пользователя.";
                intId = 0;
            } else {
                title = "Редактирование пользователя.";
                try {
                    intId = Integer.parseInt(id);
                } catch (NumberFormatException nfe) {
                    LOGGER.warning("Suspicious parameter\n" + nfe.getMessage());
                    intId = 0;
                }
            }

            User user = PostgreHbnStore.instOf().findById(new User().setId(intId));
            if (user == null) {
                user = new User();
                title = "Регистрация пользователя.";
            }

            req.setAttribute("title", title);
            req.setAttribute("user", user);
            req.getRequestDispatcher("WEB-INF/reg.jsp").forward(req, resp);
        } else {
            doPost(req, resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user;
        HttpSession sc = req.getSession();

        switch (req.getParameter("op")) {
            case "edit":
                user = new User()
                        .setId(Integer.parseInt(req.getParameter("id")))
                        .setName(req.getParameter("name"))
                        .setEmail(req.getParameter("email"))
                        .setPassword(req.getParameter("password"));
                if (PostgreHbnStore.instOf().findUserByEmail(req.getParameter("email")) != null) {
                    req.setAttribute("error", "Пользователь с таким email уже существует");
                    req.getRequestDispatcher("WEB-INF/reg.jsp").forward(req, resp);
                    return;
                }
                if (Integer.parseInt(req.getParameter("id")) == 0) {
                    PostgreHbnStore.instOf().add(user);
                } else {
                    PostgreHbnStore.instOf().update(user);
                }
                sc.setAttribute("user", user);
                break;
            case "del":
                PostgreHbnStore.instOf().delete(new User().setId(Integer.parseInt(req.getParameter("id"))));
                sc.setAttribute("user", null);
                break;
            default:
                break;
        }
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }
}
