package structures;

import java.util.Objects;

public class Department {
    private int depId;
    private String depName;
    private String leadName;
    private int leadId;

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public Department(int depId, String depName) {
        this.depId = depId;
        this.depName = depName;
    }

    public Department(int depId, String depName, String leadName) {
        this.depId = depId;
        this.depName = depName;
        this.leadName = leadName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return depId == that.depId &&
                depName.equals(that.depName) &&
                leadName.equals(that.leadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depId, depName, leadName);
    }
}
