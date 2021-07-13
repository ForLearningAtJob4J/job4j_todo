package ru.job4j.services;

import org.json.JSONArray;
import ru.job4j.model.Category;
import ru.job4j.store.PostgreHbnStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(value = "/categories/*")
public class CategoryServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new JSONArray(PostgreHbnStore.instOf().findAll(Category.class)));
    }
}
