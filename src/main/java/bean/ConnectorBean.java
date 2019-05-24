package bean;

import org.apache.log4j.Logger;
import structures.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;


public class ConnectorBean {
    private static Statement stmt;
    private boolean initialized = false;
    private static final Logger log = Logger.getLogger(ConnectorBean.class);

    public void init() {
        if (initialized) {
            return;
        }
        log.info("Bean initialization start.");
        DataSource ds;
        try {
            InitialContext ictx = new InitialContext();
            Context context = (Context) ictx.lookup("java:comp/env");
            ds = (DataSource) context.lookup("jdbc/infsyst");
        } catch (NamingException e) {
            log.warn("Failed to locate datasource.", e);
            return;
        }
        try {
            Locale.setDefault(Locale.ENGLISH);
            Connection con = ds.getConnection();
            stmt = con.createStatement();
        } catch (SQLException e) {
            log.warn("Failed to establish connection.", e);
            return;
        }
        initialized = true;
    }

    public void destroy() {
        log.debug("Bean deinitialization start.");
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("Failed to close connection!", e);
            }
        }
    }

    @org.jetbrains.annotations.Nullable
    private ResultSet query(String query) {
        log.trace("SQL query: " + query);
        ResultSet rs;
        if (!initialized) {
            log.fatal("");
            return null;
        }
        try {
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            log.warn("SQL failure during query execution.", e);
            return null;
        }
    }


    // Table Department
    private ArrayList<Department> ResultSetToDepartment(ResultSet rs) {
        ArrayList<Department> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(new Department(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("LEADNAME")
                    ));
                }
                rs.close();
            } catch (SQLException e) {
                log.error("SQL failure during ResultSet parsing.", e);
                return new ArrayList<>();
            }
        }
        return result;
    }

    public ArrayList<Department> getDepartments() {
        ResultSet rs = query("SELECT D.ID ID, D.NAME NAME, E.FIO LEADNAME FROM SYSTEM.DEPARTMENT D LEFT JOIN SYSTEM.EMPLOYEE E ON D.ID = E.DEPID AND E.STATUS = 'Lead' ORDER BY D.ID");
        return ResultSetToDepartment(rs);
    }

    public ArrayList<String> getDeps() {
        ResultSet rs = query("SELECT NAME FROM SYSTEM.DEPARTMENT");
        return ResultSetToDepsNames(rs);
    }

    private ArrayList<String> ResultSetToDepsNames(ResultSet rs) {
        ArrayList<String> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString("NAME"));
                }
                rs.close();
            } catch (SQLException e) {
                log.error("SQL failure during ResultSet parsing.", e);
                return new ArrayList<>();
            }
        }
        return result;
    }


    public int getDepartmentIdByDepName(String name) {
        ResultSet rs = query("SELECT ID FROM SYSTEM.DEPARTMENT WHERE NAME = '" + name + "' AND ROWNUM = 1");
        try {
            assert rs != null;
            rs.next();
            return rs.getInt("ID");
        } catch (SQLException e) {
            log.error("SQL failure during ResultSet parsing.", e);
        }
        return 10000000;
    }

    public void insertDepartments(ArrayList<Department> data) {
        for (Department x : data) {
            String queryString = "INSERT INTO SYSTEM.DEPARTMENT (ID, NAME) VALUES(" +
                    x.getDepId() + ", '" + x.getDepName() + "')";
            String queryStringUpd1 = "UPDATE SYSTEM.EMPLOYEE SET status = 'Worker' WHERE status = 'Lead' AND depid = " + x.getDepId();
            String queryStringUpd2 = "UPDATE SYSTEM.EMPLOYEE SET depid =" +
                    x.getDepId() + ", status = 'Lead' WHERE fio = '" + x.getLeadName() + "'";
            try {
                stmt.executeUpdate(queryString);
                stmt.executeUpdate(queryStringUpd1);
                stmt.executeUpdate(queryStringUpd2);
            } catch (SQLException e) {
                log.warn("SQL failure during Department-based insert.", e);
            }
        }
    }

    public void updateDepartments(ArrayList<Department> data) throws SQLException {
        for (Department x : data) {
            String queryString = "UPDATE SYSTEM.DEPARTMENT SET id =" +
                    x.getDepId() + ", name = '" + x.getDepName() + "' WHERE id = " + x.getDepId();
            if (!x.getLeadName().equals("")) {
                String queryString2 = "UPDATE SYSTEM.EMPLOYEE SET depid =" +
                        x.getDepId() + ", status = 'Lead' WHERE fio = '" + x.getLeadName() + "'";
                stmt.executeUpdate(queryString2);
            }
            try {
                stmt.executeUpdate(queryString);
            } catch (SQLException e) {
                log.warn("SQL failure during Department-based update.", e);
            }
        }
    }

    public void deleteDepartments(ArrayList<Department> data) {
        for (Department x : data) {
            String queryString = "DELETE FROM SYSTEM.DEPARTMENT WHERE id = " + x.getDepId();
            String queryStringUpd2 = "UPDATE SYSTEM.EMPLOYEE SET depid = 1, status = 'Worker' WHERE depid = '" + x.getDepId() + "'";
            try {
                stmt.executeUpdate(queryStringUpd2);
                stmt.executeUpdate(queryString);
            } catch (SQLException e) {
                log.warn("SQL failure during Department-based delete.", e);
            }
        }
    }

    // Table Employee
    private ArrayList<Employee> ResultSetToEmployee(ResultSet rs) {
        ArrayList<Employee> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(new Employee(
                            rs.getInt("ID"),
                            rs.getString("FIO"),
                            rs.getString("NAME"),
                            rs.getString("PHONENUMBER"),
                            rs.getString("SALARY")
                    ));
                }
                rs.close();
            } catch (SQLException e) {
                log.warn("SQL failure during ResultSet parsing.", e);
                return new ArrayList<>();
            }
        }
        return result;
    }

    public ArrayList<Employee> getEmployees() {
        ResultSet rs = query("SELECT E.ID ID, E.FIO FIO, D.NAME NAME, E.PHONENUMBER PHONENUMBER, E.SALARY SALARY" +
                " FROM SYSTEM.EMPLOYEE E, SYSTEM.DEPARTMENT D WHERE D.ID = E.DEPID ORDER BY E.ID");
        return ResultSetToEmployee(rs);
    }

    public ArrayList<String> getLeads() {
        ResultSet rs = query("SELECT FIO FROM SYSTEM.EMPLOYEE");
        return ResultSetArrLeads(rs);
    }

    private ArrayList<String> ResultSetArrLeads(ResultSet rs) {
        ArrayList<String> result = new ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    result.add(rs.getString("FIO"));
                }
                rs.close();
            } catch (SQLException e) {
                log.warn("SQL failure during ResultSet parsing.", e);
                return new ArrayList<>();
            }
        }
        return result;
    }

    public void insertEmployees(ArrayList<Employee> data) {
        for (Employee x : data) {
            String queryString = "INSERT INTO SYSTEM.EMPLOYEE (ID, FIO, DEPID, PHONENUMBER, SALARY, STATUS) VALUES(" +
                    x.getId() + ",'" + x.getFio() + "'," + x.getDepId() + ",'" + x.getPhoneNumber() + "','" + x.getSalary() + "', 'Worker')";
            try {
                stmt.executeUpdate(queryString);
            } catch (SQLException e) {
                log.warn("SQL failure during Employee-based insert.", e);
            }
        }

    }

    public void updateEmployees(ArrayList<Employee> data) {
        for (Employee x : data) {
            String queryString;
            if (x.getDepId() != 10000000) {
                queryString = "UPDATE SYSTEM.EMPLOYEE SET FIO = '" + x.getFio() + "', DEPID = " + x.getDepId() + ", PHONENUMBER = '" + x.getPhoneNumber()
                        + "', SALARY = '" + x.getSalary() + "', status = 'Worker' WHERE ID = " + x.getId();
            } else {
                queryString = "UPDATE SYSTEM.EMPLOYEE SET FIO = '" + x.getFio() + "', DEPID = 1 , PHONENUMBER = '" + x.getPhoneNumber()
                        + "', SALARY = '" + x.getSalary() + "', status = 'Worker' WHERE ID = " + x.getId();
            }
            try {
                stmt.executeUpdate(queryString);
            } catch (SQLException e) {
                log.warn("SQL failure during Employee-based update.", e);
            }
        }
    }

    public void deleteEmployees(ArrayList<Employee> data) {
        for (Employee x : data) {
            String queryString = "DELETE FROM SYSTEM.EMPLOYEE WHERE id = " + x.getId();
            try {
                stmt.executeUpdate(queryString);
            } catch (SQLException e) {
                log.warn("SQL failure during Employee-based delete.", e);
            }
        }
    }

}