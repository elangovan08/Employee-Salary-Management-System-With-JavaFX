package gui;

class Engineer extends Employee {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double overtime;

    public Engineer(String name, double salary, double overtime, String department) {
        super(name, salary, department);
        this.overtime = overtime;
    }

    public double getOvertime() { return overtime; }

    @Override
    public String toFileString() {
        return String.format("%s|%s|%s|%.2f|%.2f|%s", 
            id, name, department, salary, overtime, "Engineer");
    }
}