package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class EmployeeStorageUtil {
    public static final String FILE = "employees.dat";
    private static int lastId = 100;
    private static final Object idLock = new Object();

    static {
        lastId = findHighestId();
    }

    private static int findHighestId() {
        int max = 100;
        File file = new File(FILE);
        if (!file.exists()) {
            return max;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|", 2);
                if (parts.length > 0) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        if (id > max) max = id;
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid ID in record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employee file: " + e.getMessage());
        }
        return max;
    }

    public static String getNextId() {
        synchronized (idLock) {
            lastId++;
            return String.format("%03d", lastId);
        }
    }

    public static void saveEmployee(Employee emp) throws IOException {
        synchronized (idLock) {
            // Temporarily make file writable
            File file = new File(FILE);
            file.setWritable(true);
            
            if (isNameExists(emp.getName())) {
                file.setReadOnly();
                throw new IllegalArgumentException("Employee with this name already exists");
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
                writer.write(emp.toFileString());
                writer.newLine();
            }
            
            // Set back to read-only
            file.setReadOnly();
        }
    }

    public static boolean isNameExists(String name) {
        File file = new File(FILE);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 1 && parts[1].equalsIgnoreCase(name.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking name existence: " + e.getMessage());
        }
        return false;
    }

    public static Employee searchEmployeeById(String id) {
        File file = new File(FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(id + "|")) {
                    String[] data = line.split("\\|");
                    if (data.length == 6) {
                        String name = data[1], dept = data[2], type = data[5];
                        double salary = Double.parseDouble(data[3]);
                        double extra = Double.parseDouble(data[4]);

                        Employee emp = type.equalsIgnoreCase("Manager")
                                ? new Manager(name, salary, extra, dept)
                                : new Engineer(name, salary, extra, dept);
                        emp.id = id;
                        return emp;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error searching employee: " + e.getMessage());
        }
        return null;
    }

    public static boolean deleteEmployeeById(String id) {
        synchronized (idLock) {
            File file = new File(FILE);
            if (!file.exists()) {
                return false;
            }

            // Temporarily make file writable
            file.setWritable(true);

            List<String> lines = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(id + "|")) {
                        lines.add(line);
                    } else {
                        found = true;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error deleting employee: " + e.getMessage());
                file.setReadOnly();
                return false;
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error writing updated file: " + e.getMessage());
                    file.setReadOnly();
                    return false;
                }
            }
            
            // Set back to read-only
            file.setReadOnly();
            return found;
        }
    }

    public static boolean updateEmployeeById(String id, Employee newEmp) {
        synchronized (idLock) {
            File file = new File(FILE);
            if (!file.exists()) {
                return false;
            }

            // Temporarily make file writable
            file.setWritable(true);

            List<String> lines = new ArrayList<>();
            boolean updated = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(id + "|")) {
                        lines.add(newEmp.toFileString());
                        updated = true;
                    } else {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error updating employee: " + e.getMessage());
                file.setReadOnly();
                return false;
            }

            if (updated) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error writing updated file: " + e.getMessage());
                    file.setReadOnly();
                    return false;
                }
            }
            
            // Set back to read-only
            file.setReadOnly();
            return updated;
        }
    }

    public static List<Employee> loadAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        File file = new File(FILE);
        if (!file.exists()) {
            return employees;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length == 6) {
                    String id = data[0], name = data[1], dept = data[2], type = data[5];
                    double salary = Double.parseDouble(data[3]);
                    double extra = Double.parseDouble(data[4]);

                    Employee emp = type.equalsIgnoreCase("Manager")
                            ? new Manager(name, salary, extra, dept)
                            : new Engineer(name, salary, extra, dept);
                    emp.id = id;
                    employees.add(emp);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employees: " + e.getMessage());
        }
        return employees;
    }
} 