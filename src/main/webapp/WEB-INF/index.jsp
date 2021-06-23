<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>ToDo List Application</title>
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="img/favicon.ico" rel="icon" type="image/x-icon">
    <link href="css/index.css" rel="stylesheet">
    <script src="js/index.js"></script>
</head>
<body>

<div class="container-fluid">
        <div class="info">
            <span>${user.name}</span> | <a class="nav-link" href="auth?op=exit"> Выйти </a>
        </div>

    <h3>Новая задача</h3>
    <form class="form-horizontal">
        <div class="form-group">
            <label class="control-label col-sm-1" for="desc">Описание:</label>
            <div class="col-sm-11">
                <textarea class="form-control" id="desc" rows="5"></textarea>
            </div>
        </div>
    </form>
    <div>
        <button class="btn btn-default col-sm-offset-1 col-1"
                onclick="addTaskAjax()">Добавить задачу
        </button>
    </div>

    <h3>Список задач</h3>
    <p id="showAllWrapper"><label for="showAll">Показать все задачи<input id="showAll" onclick="updateTaskVisibility()"
                                                                          type="checkbox"></label>
        <br>
        <button class="ui-button ui-icon-refresh" id="refresh" onclick="refreshTodoList()">Обновить список</button>
    </p>
    <div id="tableWrapper">
        <img alt="LOADING" id="loading-indicator" src="img/loader.gif"/>
        <table class="table" id="table">
            <thead>
            <tr>
                <th>Описание задачи</th>
                <th>Дата создания</th>
                <th>Выполнено</th>
                <th>Создатель</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>