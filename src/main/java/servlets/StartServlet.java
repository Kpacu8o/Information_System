package servlets;

import bean.ConnectorBean;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import structures.Department;
import structures.Employee;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class StartServlet extends HttpServlet {

    private static ConnectorBean connectorBean;

    @Override
    public void init(ServletConfig config) {
        ApplicationContext context = (ApplicationContext) config.getServletContext().getAttribute("applicationContext");
        config.getServletContext().setAttribute("StartServlet", this);
        connectorBean = (ConnectorBean) context.getBean("connectorBean");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryType = "";
        JSONArray queryData = null;
        JSONObject currentQuery;
        try {

            currentQuery = query(request);
            if (currentQuery != null) {
                queryType = currentQuery.getString("type");
                queryData = currentQuery.getJSONArray("data");
            }

            if (queryType.equals("update_dep")) {
                serveDepartUpdate(queryData);
            } else if (queryType.equals("update_empl")) {
                serveEmplUpdate(queryData);
            }

            HttpSession session = request.getSession();
            session.removeAttribute("department");
            session.removeAttribute("leads");
            session.removeAttribute("employee");
            session.removeAttribute("deps");
            session.setAttribute("department", connectorBean.getDepartments());
            session.setAttribute("leads", toSelectArray(connectorBean.getLeads()));
            session.setAttribute("employee", connectorBean.getEmployees());
            session.setAttribute("deps", toSelectArray(connectorBean.getDeps()));

            request.getRequestDispatcher("/views/TEST.jsp").forward(request, response);
        } catch (NullPointerException | SQLException e) {
            request.getRequestDispatcher("/views/TEST.jsp").forward(request, response);
        }
    }

    private void serveDepartUpdate(JSONArray queryData) throws SQLException {
        ArrayList<Department> array = new ArrayList<>();
        for (Object i : queryData) {
            JSONObject x = (JSONObject) i;
            int x_id = x.getInt("id");
            String name = x.getString("name");
            String lead = x.getString("lead");
            array.add(new Department(x_id, name, lead));
        }

        doCaseMethodDep(connectorBean.getDepartments(), array);
    }

    private void serveEmplUpdate(JSONArray queryData) throws SQLException {
        ArrayList<Employee> array = new ArrayList<>();
        for (Object i : queryData) {
            JSONObject x = (JSONObject) i;
            int x_id = x.getInt("id");
            String fio = x.getString("fio");
            String department = x.getString("department");
            String phone = x.getString("phone");
            String salary = x.getString("salary");
            array.add(new Employee(x_id, fio, department, phone, salary));
        }
        doCaseMethodEmpl(connectorBean.getEmployees(), array);
    }

    private JSONObject query(HttpServletRequest req) {
        String queryString = req.getParameter("query");
        if ((queryString != null) && (!queryString.equals(""))) {
            return new JSONObject(queryString);
        } else {
            return null;
        }
    }

    private void doCaseMethodDep(ArrayList<Department> dbDataList, ArrayList<Department> uiDataList) throws SQLException {
        ArrayList<Department> listForUpdate = new ArrayList<>();
        ArrayList<Department> listForInsert = new ArrayList<>();
        ArrayList<Department> listForDelete = new ArrayList<>();
        Integer[] idsDB = new Integer[100];
        for (int i = 0; i < dbDataList.size(); i++) {
            idsDB[i] = (dbDataList.get(i).getDepId());
        }

        for (Department department : uiDataList) {
            if (!contains2(idsDB, department.getDepId())) {
                listForInsert.add(department);
            } else if (!department.equals(getDepartmentById(dbDataList, department.getDepId()))) {
                listForUpdate.add(department);
            }
        }
        for (Department department : dbDataList) {
            if (getDepartmentById(uiDataList, department.getDepId()) == null) {
                listForDelete.add(department);
            }
        }
        connectorBean.updateDepartments(listForUpdate);
        connectorBean.insertDepartments(listForInsert);
        connectorBean.deleteDepartments(listForDelete);
    }

    private void doCaseMethodEmpl(ArrayList<Employee> dbDataList, ArrayList<Employee> uiDataList) throws SQLException {
        ArrayList<Employee> listForUpdate = new ArrayList<>();
        ArrayList<Employee> listForInsert = new ArrayList<>();
        ArrayList<Employee> listForDelete = new ArrayList<>();
        Integer[] idsDB = new Integer[100];
        for (int i = 0; i < dbDataList.size(); i++) {
            idsDB[i] = (dbDataList.get(i).getId());
        }

        for (Employee employee : uiDataList) {
            if (!contains2(idsDB, employee.getId())) {
                employee.setDepId(connectorBean.getDepartmentIdByDepName(employee.getDepName()));
                listForInsert.add(employee);
            } else if (!employee.equals(getEmployeeById(dbDataList, employee.getId()))) {
                employee.setDepId(connectorBean.getDepartmentIdByDepName(employee.getDepName()));
                listForUpdate.add(employee);
            }
        }
        for (Employee employee : dbDataList) {
            if (getEmployeeById(uiDataList, employee.getId()) == null) {
                listForDelete.add(employee);
            }
        }
        connectorBean.updateEmployees(listForUpdate);
        connectorBean.insertEmployees(listForInsert);
        connectorBean.deleteEmployees(listForDelete);
    }

    private Department getDepartmentById(ArrayList<Department> listDep, int id) {
        for (Department dep : listDep) {
            if (dep.getDepId() == id) {
                return dep;
            }
        }
        return null;
    }

    private Employee getEmployeeById(ArrayList<Employee> arrayList, int id) {
        for (Employee employee : arrayList) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    private ArrayList<String> toSelectArray(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.set(i, "\"" + arrayList.get(i) + "\"");
        }
        return arrayList;
    }

    private static <T> boolean contains2(final T[] array, final T v) {
        if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {
            for (final T e : array)
                if (e == v || v.equals(e))
                    return true;
        }
        return false;
    }
}
