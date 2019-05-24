<%--@elvariable id="deps" type="java.util.ArrayList"--%>
<%--@elvariable id="leads" type="java.util.ArrayList"--%>
<%--@elvariable id="department" type="java.util.ArrayList"--%>
<%--@elvariable id="employee" type="java.util.ArrayList"--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Информационная система</title>
    <link rel="stylesheet" type="text/css" href="../main.css"/>
    <script type="text/javascript">
        window.onload = function () {
            var table = document.getElementById("table1");
            for (var i = 1; i < table.rows.length; i++) {
                addListenersToLastEditCell(i);
                addListenersToLastRow(i, "table1");
            }
            table = document.getElementById("table2");
            for (var j = 1; j < table.rows.length; j++) {
                addListenersToLastEditCellEmpl(j);
                addListenersToLastRow(j, "table2");
            }

        };

        function addListenersToLastRow(index, tableId) {
            var r, c, selector = "rgba(115, 255, 211, 0.44)", editor = "rgba(255, 242, 3, 0.44)",
                table = document.getElementById(tableId);
            r = table.rows[index];
            c = r.cells;
            for (var i = 0; i < c.length - 1; i++) {
                c[i].addEventListener('click', function () {
                    if (r.style.backgroundColor !== editor) {
                        r.style.backgroundColor = (r.style.backgroundColor === selector) ? 'white' : selector;
                    }
                })
            }
        }

        function addListenersToLastEditCell(index) {
            var r, c, editor = "rgba(255, 242, 3, 0.44)", inp, sel, opt, deps = ${leads},
                table = document.getElementById("table1"), txtsize;
            r = table.rows[index];
            c = r.cells[r.cells.length - 1];
            txtsize = (r.cells[1].innerText.length + 1) * 8;
            c.addEventListener('click', function () {
                if (r.style.backgroundColor === editor) {
                    r.style.backgroundColor = 'white';
                    r.cells[1].innerText = r.cells[1].children[0].value;
                    r.cells[2].innerText = r.cells[2].children[0].options[r.cells[2].children[0].selectedIndex].text;
                    r.cells[r.cells.length - 1].innerText = "Изменить";
                } else {
                    r.style.backgroundColor = editor;
                    inp = document.createElement("INPUT");
                    inp.setAttribute("style", "width:" + txtsize + "px");
                    inp.setAttribute("type", "text");
                    inp.setAttribute("onkeydown", "this.style.width = ((this.value.length + 1) * 8) + 'px';");
                    inp.value = r.cells[1].innerText;
                    r.cells[1].innerText = "     ";
                    r.cells[1].appendChild(inp);
                    sel = document.createElement("SELECT");

                    for (var i = 0; i < deps.length; i++) {
                        opt = document.createElement("OPTION");
                        opt.innerText = deps[i];
                        if (opt.innerText === r.cells[2].innerText) {
                            opt.setAttribute("selected", "selected");
                        }
                        sel.appendChild(opt);
                    }
                    r.cells[2].innerText = "     ";
                    r.cells[2].appendChild(sel);
                    r.cells[r.cells.length - 1].innerText = "Сохранить";
                }

            })
        }

        function addListenersToLastEditCellEmpl(index) {
            var r, c, editor = "rgba(255, 242, 3, 0.44)", inp, sel, opt, leads = ${deps},
                table = document.getElementById("table2"), txtsize;
            r = table.rows[index];
            c = r.cells[r.cells.length - 1];
            txtsize = (r.cells[1].innerText.length + 1) * 8;
            c.addEventListener('click', function () {
                if (r.style.backgroundColor === editor) {
                    r.style.backgroundColor = 'white';
                    r.cells[1].innerText = r.cells[1].children[0].value;
                    r.cells[2].innerText = r.cells[2].children[0].options[r.cells[2].children[0].selectedIndex].text;
                    r.cells[3].innerText = r.cells[3].children[0].value;
                    r.cells[4].innerText = r.cells[4].children[0].value;
                    r.cells[r.cells.length - 1].innerText = "Изменить";
                } else {
                    r.style.backgroundColor = editor;
                    //cell 1
                    inp = document.createElement("INPUT");
                    inp.setAttribute("style", "width:" + txtsize + "px");
                    inp.setAttribute("type", "text");
                    inp.setAttribute("onkeydown", "this.style.width = ((this.value.length + 1) * 8) + 'px';");
                    inp.value = r.cells[1].innerText;
                    r.cells[1].innerText = "     ";
                    r.cells[1].appendChild(inp);
                    //cell 2
                    sel = document.createElement("SELECT");
                    for (var i = 0; i < leads.length; i++) {
                        opt = document.createElement("OPTION");
                        opt.innerText = leads[i];
                        if (opt.innerText === r.cells[2].innerText) {
                            opt.setAttribute("selected", "selected");
                        }
                        sel.appendChild(opt);
                    }
                    r.cells[2].innerText = "     ";
                    r.cells[2].appendChild(sel);
                    //cell 3
                    txtsize = (r.cells[3].innerText.length + 1) * 8;
                    inp = document.createElement("INPUT");
                    inp.setAttribute("style", "width:" + txtsize + "px");
                    inp.setAttribute("type", "text");
                    inp.setAttribute("onkeydown", "this.style.width = ((this.value.length + 1) * 8) + 'px';");
                    inp.value = r.cells[3].innerText;
                    r.cells[3].innerText = "     ";
                    r.cells[3].appendChild(inp);
                    //cell 4
                    txtsize = (r.cells[4].innerText.length + 1) * 8;
                    inp = document.createElement("INPUT");
                    inp.setAttribute("style", "width:" + txtsize + "px");
                    inp.setAttribute("type", "text");
                    inp.setAttribute("onkeydown", "this.style.width = ((this.value.length + 1) * 8) + 'px';");
                    inp.value = r.cells[4].innerText;
                    r.cells[4].innerText = "     ";
                    r.cells[4].appendChild(inp);
                    //cell 5
                    r.cells[r.cells.length - 1].innerText = "Сохранить";
                }

            })
        }

        function getNewId(tableId) {
            var table = document.getElementById(tableId), newId;
            if (table.rows[table.rows.length - 1].cells[0].innerHTML === "ID") {
                newId = 1;
            } else {
                newId = parseInt(table.rows[table.rows.length - 1].cells[0].innerHTML, 10) + 1;
            }
            return newId;
        }

        function refreshPage() {
            window.location.reload();
        }

        function deleteSelectedRows(tableId) {
            var tableHeaderRowCount = 1;
            var table = document.getElementById(tableId), selector = "rgba(115, 255, 211, 0.44)";
            var rowCount = table.rows.length;
            for (var i = tableHeaderRowCount; i < rowCount; i++) {
                if (table.rows[tableHeaderRowCount].style.backgroundColor === selector) {
                    table.deleteRow(tableHeaderRowCount);
                } else {
                    tableHeaderRowCount++;
                }
            }
        }

        function buildTableDep() {
            var table = document.getElementById('table1'), array = [];
            for (var i = 1; i < table.rows.length; i++) {
                var row = table.rows[i];
                var id = row.cells[0].innerText;
                var name = row.cells[1].innerText;
                var lead = row.cells[2].innerText;
                array.push({
                    id: id,
                    name: name,
                    lead: lead
                });
            }
            return array;
        }

        function buildTableEmpl() {
            var table = document.getElementById('table2'), array = [];
            for (var i = 1; i < table.rows.length; i++) {
                var row = table.rows[i];
                var id = row.cells[0].innerText;
                var fio = row.cells[1].innerText;
                var department = row.cells[2].innerText;
                var phone = row.cells[3].innerText;
                var salary = row.cells[4].innerText;
                array.push({
                    id: id,
                    fio: fio,
                    department: department,
                    phone: phone,
                    salary: salary
                });
            }
            return array;
        }

        function saveData(type) {
            var table, outfall;
            if (type === "dep") {
                table = buildTableDep();
                outfall = {
                    type: "update_dep",
                    data: table
                };
                document.getElementById("query").value = JSON.stringify(outfall);
            }

            if (type === "empl") {
                table = buildTableEmpl();
                outfall = {
                    type: "update_empl",
                    data: table
                };
                document.getElementById("query2").value = JSON.stringify(outfall);
            }
        }

        function addNewRowDep() {
            var row, td1, td2, td3, td4, table = document.getElementById("table1");
            row = document.createElement("TR");
            td1 = document.createElement("TD");
            td1.setAttribute("style", "display:none;");
            td2 = document.createElement("TD");
            td3 = document.createElement("TD");
            td4 = document.createElement("TD");
            td1.appendChild(document.createTextNode(getNewId("table1")));
            //td2.appendChild(document.createTextNode("New name"));
            //td3.appendChild(document.createTextNode("New name"));
            td4.appendChild(document.createTextNode("Изменить"));
            row.appendChild(td1);
            row.appendChild(td2);
            row.appendChild(td3);
            row.appendChild(td4);
            table.appendChild(row);
            addListenersToLastRow(table.rows.length - 1, "table1");
            addListenersToLastEditCell(table.rows.length - 1);
        }

        function addNewRowEmpl() {
            var row, td1, td2, td3, td4, td5, td6, table = document.getElementById("table2");
            row = document.createElement("TR");
            td1 = document.createElement("TD");
            td1.setAttribute("style", "display:none;");
            td2 = document.createElement("TD");
            td3 = document.createElement("TD");
            td4 = document.createElement("TD");
            td5 = document.createElement("TD");
            td6 = document.createElement("TD");
            td1.appendChild(document.createTextNode(getNewId("table2")));
            //td2.appendChild(document.createTextNode("New name"));
            //td3.appendChild(document.createTextNode("New name"));
            //td4.appendChild(document.createTextNode("New value"));
            //td5.appendChild(document.createTextNode("New value"));
            td6.appendChild(document.createTextNode("Изменить"));
            row.appendChild(td1);
            row.appendChild(td2);
            row.appendChild(td3);
            row.appendChild(td4);
            row.appendChild(td5);
            row.appendChild(td6);
            table.appendChild(row);
            addListenersToLastRow(table.rows.length - 1, "table2");
            addListenersToLastEditCellEmpl(table.rows.length - 1);
        }

    </script>
</head>
<body>
<div id="maible1">
    <table id="table1">
        <caption>Отделы</caption>
        <tr>
            <td style="display:none;">ID</td>
            <td>Название отдела</td>
            <td colspan="2">Начальник отдела</td>
        </tr>

        <c:forEach items="${department}" var="item">
            <tr>
                <td style="display:none;">${item.depId}</td>
                <td>${item.depName}</td>
                <td>${item.leadName}</td>
                <td>Изменить</td>
            </tr>
        </c:forEach>
    </table>
    <div onclick="addNewRowDep()">Добавить новый отдел</div>
    <div onclick="deleteSelectedRows('table1')">Удалить выделенное</div>
    <div onclick="refreshPage()">Отмена</div>
    <form>
        <button onclick="saveData('dep')" formmethod="get" rel="shortcut icon" formaction="/start">Сохранить всё</button>
        <input type="hidden" name="query" id="query"/>
    </form>
</div>

<div id="maible2">
    <table id="table2">
        <caption>Сотрудники</caption>
        <tr>
            <td style="display:none;">ID</td>
            <td>ФИО</td>
            <td>Отдел</td>
            <td>Телефонный номер</td>
            <td colspan="2">Зарплата</td>
        </tr>
        <c:forEach items="${employee}" var="item">
            <tr>
                <td style="display:none;">${item.id}</td>
                <td>${item.fio}</td>
                <td>${item.depName}</td>
                <td>${item.phoneNumber}</td>
                <td>${item.salary}</td>
                <td>Изменить</td>
            </tr>
        </c:forEach>
    </table>
    <div onclick="addNewRowEmpl()">Добавить сотрудника</div>
    <div onclick="deleteSelectedRows('table2')">Удалить выделенное</div>
    <div onclick="refreshPage()">Отмена</div>
    <form>
        <button onclick="saveData('empl')" formmethod="get" rel="shortcut icon" formaction="/start">Сохранить всё</button>
        <input type="hidden" name="query" id="query2"/>
    </form>
</div>

</body>
</html>
