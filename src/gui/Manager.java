package gui;

class Manager extends Employee {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double bonus;

    public Manager(String name, double salary, double bonus, String department) {
        super(name, salary, department);
        this.bonus = bonus;
    }

    public double getBonus() { return bonus; }

    @Override
    public String toFileString() {
        return String.format("%s|%s|%s|%.2f|%.2f|%s", 
            id, name, department, salary, bonus, "Manager");
    }
}