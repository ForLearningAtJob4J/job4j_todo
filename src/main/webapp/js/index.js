$(document).ready(function () {
    fillCategories();
    refreshTodoList();
});

function refreshTodoList() {
    $("#loading-indicator").show();
    $.ajax({
        type: 'GET',
        url: 'tasks',
        dataType: 'json'
    }).done(function (data) {
        $('#table tbody tr').remove();
        for (let i = 0; i < data.length; i++) {
            addRow(data[i]);
        }
        $("#loading-indicator").hide();
    }).fail(function (error) {
        $("#loading-indicator").hide();
        console.log('Error in refreshTodoList: ' + error);
    });
}

function fillCategories() {
    $.ajax({
        type: 'GET',
        url: 'categories',
        dataType: 'json'
    }).done(function (data) {
        $('#cIds option').remove();
        for (let i = 0; i < data.length; i++) {
            $('#cIds').append(`<option value="${data[i].id}">${data[i].name}</option>`);
        }
    }).fail(function (error) {
        console.log('Error in fillCategories: ' + error);
    });
}

function updateTaskVisibility() {
    const showAll = document.querySelector('#showAll').checked;
    let table = document.querySelector('#table tbody');

    for (let row of table.rows) {
        row.hidden = !showAll && row.lastChild.previousSibling.previousSibling.lastChild.checked;
    }
}

function validate() {

    if ($('#desc').val() === '') {
        alert('Заполните описание задачи');
        return false;
    }

    return true;
}

function addRow(task) {
    console.log(task);
    $('#table tbody').append(
        '<tr ' + ((!document.querySelector('#showAll').checked && task.done === true) ? "hidden" : "") + '>' +
        '<td>' + task.desc + '</td>' +
        '<td>' + task.categories.reduce((acc, cur) => acc + cur.name + '<br>', '')+ '</td>' +
        '<td>' + new Date(task.created).toLocaleString("ru-RU") + '</td>' +
        '<td>' +
        '   <input onclick="changeTaskStateAjax(this)" data-id="' + task.id + '" type="checkbox" ' + (task.done === true ? "checked" : "") + '></td>' +
        '<td>' + task.user.name + '</td>' +
        '<td><img class="delete-button" src="img/remove.svg" onclick="deleteTaskAjax(this)" data-id="' + task.id + '" alt="del"></td></tr>'
    );
}

function addTaskAjax() {
    if (validate()) {
        $.ajax({
            type: 'POST',
            url: 'tasks',
            data: JSON.stringify({desc: $('#desc').val(), categories: $('#cIds').val()}),
            dataType: 'json'
        }).done(data => {
            addRow(data);
            $('#desc').val('');
            document.querySelector('#cIds').selectedIndex = -1;
            document.querySelector('#desc').select();
        }).fail((e) => {
                console.log(e);
            }
        )
    }
    console.log('addTaskAjax().return');
    // return false;
}

function changeTaskStateAjax(el) {
    $("#loading-indicator").show();
    $.ajax({
        type: 'PUT',
        url: 'tasks/' + el.dataset.id,
        data: JSON.stringify({done: el.checked}),
        dataType: 'json',
        statusCode: {
            403: () => {
                alert('Отметка выполнения запрещена! Задача принадлежит другому пользователю!');
                refreshTodoList();
            }
        }
    }).done(data => {
        $("#loading-indicator").hide();
        el.parentElement.parentElement.hidden = !document.querySelector('#showAll').checked && data.done;
    }).fail((e) => {
            console.log(e);
            refreshTodoList();
        }
    )
}

function deleteTaskAjax(el) {
    if (confirm('Вы действительно хотите удалить задачу? ')) {
        $("#loading-indicator").show();
        $.ajax({
            type: 'DELETE',
            url: 'tasks/' + el.dataset.id,
            statusCode: {
                403: () => alert('Удаление запрещено! Задача принадлежит другому пользователю!')
            }
        }).done(data => {
            $('#loading-indicator').hide();
            $(`input[data-id="${data}"]`)[0].parentElement.parentElement.remove();
        }).fail((e) => {
                console.log(e);
                refreshTodoList();
            }
        )
    }
}