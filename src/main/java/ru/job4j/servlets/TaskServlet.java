package ru.job4j.servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.job4j.model.Task;
import ru.job4j.store.PostgreHbnStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(value = "/tasks/*")
public class TaskServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TaskServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(new JSONArray(PostgreHbnStore.instOf().findAllTasks()));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String rURI = req.getRequestURI();
        String strTaskCode = rURI.substring(rURI.lastIndexOf("/tasks") + 6);
        if (strTaskCode.startsWith("/")) {
            strTaskCode = strTaskCode.substring(1);
        }

        JSONObject object = new JSONObject(req.getReader().lines().collect(Collectors.joining()));

        if (strTaskCode.isEmpty()) {                        // new task
            Task task = new Task();
            task.setId(0);
            task.setDesc(object.getString("desc"));

            task = PostgreHbnStore.instOf().add(task);
            resp.getWriter().print(new JSONObject(task));
        } else {                                           // update task
            try {
                Task task = PostgreHbnStore.instOf().findById(Integer.parseInt(strTaskCode));
                if (task != null) {
                    if (object.has("desc")) {
                        task.setDesc(object.getString("desc"));
                    }
                    if (object.has("done")) {
                        task.setDone(object.getBoolean("done"));
                    }
                    PostgreHbnStore.instOf().update(task);
                    resp.getWriter().print(new JSONObject(task));
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String rURI = req.getRequestURI();
        String strTaskCode = rURI.substring(rURI.lastIndexOf("/tasks") + 6);
        if (strTaskCode.startsWith("/")) {
            strTaskCode = strTaskCode.substring(1);
        }

        if (!strTaskCode.isEmpty()) {
            try {
                PostgreHbnStore.instOf().delete(Integer.parseInt(strTaskCode));
                resp.getWriter().print(strTaskCode);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                resp.setStatus(400);
            }
        } else {
            resp.setStatus(400);
        }
    }
}