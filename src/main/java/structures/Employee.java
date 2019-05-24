package structures;

import java.util.Objects;

public class Employee {
    private int id;
    private String fio;
    private int depId;
    private String depName;
    private String phoneNumber;
    private String salary;
    private String status;

    public Employee(int id, String fio, String depName, String phoneNumber, String salary) {
        this.id = id;
        this.fio = fio;
        this.depName = depName;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                Objects.equals(fio, employee.fio) &&
                Objects.equals(depName, employee.depName) &&
                Objects.equals(phoneNumber, employee.phoneNumber) &&
                Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fio, depName, phoneNumber, salary);
    }
}
