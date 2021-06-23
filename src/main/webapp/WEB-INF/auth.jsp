<%@ page contentType="text/html; charset=UTF-8" %>

<!doctype html>
<html lang="en">
<head>
    <title>ToDo List Application</title>
    <link href="img/favicon.ico" rel="icon" type="image/x-icon">
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <script>
        $(document).ready(function () {
            $("#loading-indicator").hide();
        });

        function validate() {
            let message = '';
            if ($('#email').val() === '') {
                message += 'Имя\n';
            }
            if ($('#password').val() === '') {
                message += 'Адрес электронной почты\n';
            }

            if (message === '') {
                $("#loading-indicator").show();
                return true;
            }
            message = "Заполните следующие поля:\n\n" + message;
            alert(message);

            return false;
        }
    </script>
</head>
<body>
<div class="container pt-3">

    <div class="row">
        <div class="card" style="width: 100%">
            <div style="color: red;">
                ${requestScope.error}
            </div>
            <div class="card-header">
                Авторизация | <a href="reg">Регистрация</a>
            </div>
            <div class="card-body">
                <form action="auth" method="post">
                    <div class="form-group">
                        <label for="email">Почта</label>
                        <input id="email" type="text" class="form-control" name="email">
                    </div>
                    <div class="form-group">
                        <label for="password">Пароль</label>
                        <input id="password" type="text" class="form-control" name="password">
                    </div>
                    <button onclick="return validate();" type="submit" class="btn btn-primary">Войти</button>
                    <img alt="LOADING" id="loading-indicator" src="img/loader.gif"/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>