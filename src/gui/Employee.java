package gui;

import java.io.Serializable;

abstract class Employee implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
    protected String name;
    protected double salary;
    protected String department;

    public Employee(String name, double salary, String department) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }
        this.name = name.trim();
        this.salary = salary;
        this.department = department;
        this.id = EmployeeStorageUtil.getNextId();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public abstract String toFileString();
}
