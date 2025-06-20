package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.ScrollPane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class EmployeeSalaryGUI extends Application {

    private TextField nameField, salaryField, extraField, idField;
    private ComboBox<String> deptCombo;
    private RadioButton managerRadio, engineerRadio;
    private Button calculateBtn, saveBtn, searchBtn, deleteBtn, updateBtn, resetBtn, showAllBtn, viewDatasetBtn;
    private TextArea resultArea;
    private Employee currentLoadedEmployee = null;

    public static void main(String[] args) {
        launch(args);
    }

   
    @SuppressWarnings("unused")
	@Override
    public void start(Stage stage) {
        stage.setTitle("Employee Salary System");

        // Initialize the read-only data file
        initializeDataFile();

        // Create UI components
        Label typeLabel = new Label("Type:");
        ToggleGroup group = new ToggleGroup();
        managerRadio = new RadioButton("Manager");
        engineerRadio = new RadioButton("Engineer");
        managerRadio.setToggleGroup(group);
        engineerRadio.setToggleGroup(group);
        managerRadio.setSelected(true);

        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label deptLabel = new Label("Department:");
        deptCombo = new ComboBox<>();
        deptCombo.getItems().addAll("IT", "HR", "Finance", "Marketing");
        deptCombo.getSelectionModel().selectFirst();

        Label salaryLabel = new Label("Monthly Salary:");
        salaryField = new TextField();

        Label extraLabel = new Label("Bonus:");
        extraField = new TextField();

        // Create buttons
        calculateBtn = new Button("Calculate Salary");
        saveBtn = new Button("Save");
        searchBtn = new Button("Search by ID");
        deleteBtn = new Button("Delete by ID");
        updateBtn = new Button("Update by ID");
        resetBtn = new Button("Reset");
        showAllBtn = new Button("Show All");
        viewDatasetBtn = new Button("View Dataset");

        // Set button sizes and styles
        String buttonStyle = "-fx-background-color: #3a89ff; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;";
        String hoverStyle = "-fx-background-color: #2a79ef;";
        
        calculateBtn.setMinWidth(130);
        saveBtn.setMinWidth(80);
        resetBtn.setMinWidth(80);
        showAllBtn.setMinWidth(90);
        viewDatasetBtn.setMinWidth(100);
        
        searchBtn.setMinWidth(100);
        deleteBtn.setMinWidth(100);
        updateBtn.setMinWidth(100);

        for (Button btn : new Button[]{calculateBtn, saveBtn, resetBtn, showAllBtn, viewDatasetBtn, 
                                     searchBtn, deleteBtn, updateBtn}) {
            btn.setStyle(buttonStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
        }

        idField = new TextField();
        idField.setPromptText("Enter ID");

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(200);
        resultArea.setStyle("-fx-font-family: monospace;");

        // Layout setup
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(15));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setAlignment(Pos.CENTER);

     // Add column constraints with reduced widths
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(100);  // Reduced from 30% to fixed 100px
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150);  // Reduced middle column
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(150);  // Reduced right column
        inputGrid.getColumnConstraints().addAll(col1, col2, col3);

        // Add components to grid with proper alignment
        inputGrid.add(typeLabel, 0, 0);
        GridPane.setHalignment(typeLabel, HPos.RIGHT);  // Right-align labels
        inputGrid.add(managerRadio, 1, 0);
        inputGrid.add(engineerRadio, 2, 0);

        inputGrid.add(nameLabel, 0, 1);
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        nameField.setPrefWidth(200);  // Set fixed width for text fields
        inputGrid.add(nameField, 1, 1, 2, 1);

        inputGrid.add(deptLabel, 0, 2);
        GridPane.setHalignment(deptLabel, HPos.RIGHT);
        deptCombo.setPrefWidth(200);
        inputGrid.add(deptCombo, 1, 2, 2, 1);

        inputGrid.add(salaryLabel, 0, 3);
        GridPane.setHalignment(salaryLabel, HPos.RIGHT);
        salaryField.setPrefWidth(200);
        inputGrid.add(salaryField, 1, 3, 2, 1);

        inputGrid.add(extraLabel, 0, 4);
        GridPane.setHalignment(extraLabel, HPos.RIGHT);
        extraField.setPrefWidth(200);
        inputGrid.add(extraField, 1, 4, 2, 1);
        
        // Action buttons HBox with improved layout
        HBox actions = new HBox(8, calculateBtn, saveBtn, resetBtn, showAllBtn, viewDatasetBtn);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(5, 0, 5, 0)); // Vertical padding
        inputGrid.add(actions, 0, 5, 3, 1);

        // ID operations HBox
        inputGrid.add(new Label("Employee ID:"), 0, 6);
        inputGrid.add(idField, 1, 6);
        HBox idOps = new HBox(8, searchBtn, deleteBtn, updateBtn);
        idOps.setAlignment(Pos.CENTER);
        idOps.setPadding(new Insets(5, 0, 5, 0)); // Vertical padding
        inputGrid.add(idOps, 1, 7, 2, 1);

        // Main layout
        VBox root = new VBox(15, inputGrid, resultArea);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.setStyle("""
            -fx-background-color: linear-gradient(
                to bottom right,
                #e0f2ff 0%,
                #8bc8ff 50%,
                #3a89ff 100%
            );
        """);

        // ========== EVENT HANDLERS ==========
        group.selectedToggleProperty().addListener((obs, o, n) -> {
            extraLabel.setText(managerRadio.isSelected() ? "Bonus:" : "Overtime Hours:");
        });

        calculateBtn.setOnAction(e -> calculate());
        saveBtn.setOnAction(e -> save());

        searchBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Employee emp = EmployeeStorageUtil.searchEmployeeById(id);
            if (emp != null) {
                resultArea.setText(formatEmployeeDetails(emp));
            } else {
                resultArea.setText("No employee found with ID: " + id);
            }
        });

        deleteBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            boolean deleted = EmployeeStorageUtil.deleteEmployeeById(id);
            resultArea.setText(deleted ? "Deleted employee: " + id : "ID not found: " + id);
            if (deleted) resetForm();
        });

        updateBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            Employee emp = EmployeeStorageUtil.searchEmployeeById(id);
            if (emp != null) {
                currentLoadedEmployee = emp;
                loadForm(emp);
                resultArea.setText("Update Mode: Modify values and press Save to update.");
            } else {
                resultArea.setText("Employee not found for update.");
            }
        });

        resetBtn.setOnAction(e -> resetForm());
        showAllBtn.setOnAction(e -> showAllEmployees());
        viewDatasetBtn.setOnAction(e -> showDatasetFile());
        // ========== END EVENT HANDLERS ==========

        stage.setScene(new Scene(root, 850, 700));
        stage.show();
    }
    private void initializeDataFile() {
        File file = new File(EmployeeStorageUtil.FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating data file: " + e.getMessage());
            }
        }
        // Set file to read-only
        file.setReadOnly();
    }

    private void showDatasetFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(EmployeeStorageUtil.FILE)));
            
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Dataset File Content");
            
            TextArea textArea = new TextArea(content);
            textArea.setEditable(false);
            textArea.setStyle("-fx-font-family: monospace;");
            textArea.setWrapText(true);
            
            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            
            Scene scene = new Scene(scrollPane, 600, 400);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            resultArea.setText("Error reading dataset file: " + e.getMessage());
        }
    }

    private void calculate() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
            
            String dept = deptCombo.getValue();
            double salary = Double.parseDouble(salaryField.getText().trim());
            double extra = Double.parseDouble(extraField.getText().trim());

            Employee emp = managerRadio.isSelected()
                    ? new Manager(name, salary, extra, dept)
                    : new Engineer(name, salary, extra, dept);

            double base = salary * 12;
            double allowance = 0.1 * base;
            double bonusOrOT = (emp instanceof Manager) ? ((Manager) emp).getBonus() : ((Engineer) emp).getOvertime() * 100;
            double gross = base + bonusOrOT + allowance;
            double tax = 0.1 * gross;
            double net = gross - tax;

            resultArea.setText(String.format("""
                Salary Calculation:
                -------------------
                ID        : %s
                Name      : %s
                Department: %s
                Type      : %s

                Base      : ₹%.2f
                Bonus/OT  : ₹%.2f
                Allowance : ₹%.2f
                Gross     : ₹%.2f
                Tax       : ₹%.2f
                Net       : ₹%.2f
            """, emp.getId(), name, dept, emp.getClass().getSimpleName(), 
            base, bonusOrOT, allowance, gross, tax, net));
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void save() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
            
            if (currentLoadedEmployee == null && EmployeeStorageUtil.isNameExists(name)) {
                throw new IllegalArgumentException("Employee with this name already exists");
            }
            
            String dept = deptCombo.getValue();
            double salary = Double.parseDouble(salaryField.getText().trim());
            double extra = Double.parseDouble(extraField.getText().trim());

            Employee emp = managerRadio.isSelected()
                    ? new Manager(name, salary, extra, dept)
                    : new Engineer(name, salary, extra, dept);

            if (currentLoadedEmployee != null) {
                emp.id = currentLoadedEmployee.getId();
                if (EmployeeStorageUtil.updateEmployeeById(emp.getId(), emp)) {
                    resultArea.setText("Employee updated successfully.\n" + formatEmployeeDetails(emp));
                } else {
                    resultArea.setText("Update failed.");
                }
                currentLoadedEmployee = null;
            } else {
                EmployeeStorageUtil.saveEmployee(emp);
                resultArea.setText("Employee saved successfully.\n" + formatEmployeeDetails(emp));
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showAllEmployees() {
        List<Employee> all = EmployeeStorageUtil.loadAllEmployees();
        if (all.isEmpty()) {
            resultArea.setText("No employees stored.");
            return;
        }

        // Sort employees by ID in ascending order
        all.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getId())));

        StringBuilder sb = new StringBuilder();
        List<String> employeeBlocks = new ArrayList<>();
        
        // Format all employees
        for (Employee emp : all) {
            employeeBlocks.add(formatEmployeeForGrid(emp));
        }
        
        // Arrange in 2-column grid
        int cols = 2;
        for (int i = 0; i < employeeBlocks.size(); i += cols) {
            List<String> rowBlocks = employeeBlocks.subList(i, Math.min(i + cols, employeeBlocks.size()));
            
            List<List<String>> employeeLines = new ArrayList<>();
            int maxLines = 0;
            for (String block : rowBlocks) {
                List<String> lines = Arrays.asList(block.split("\n"));
                employeeLines.add(lines);
                maxLines = Math.max(maxLines, lines.size());
            }
            
            // Build the row line by line
            for (int lineNum = 0; lineNum < maxLines; lineNum++) {
                for (int empNum = 0; empNum < rowBlocks.size(); empNum++) {
                    List<String> lines = employeeLines.get(empNum);
                    String line = (lineNum < lines.size()) ? lines.get(lineNum) : "";
                    sb.append(String.format("%-40s", line));
                    
                    if (empNum < rowBlocks.size() - 1) {
                        sb.append("   ");
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        resultArea.setText(sb.toString());
    }

    private String formatEmployeeForGrid(Employee emp) {
        double annualBase = emp.salary * 12;
        double allowance = annualBase * 0.1;
        double specialPay = (emp instanceof Manager) ? 
            ((Manager) emp).getBonus() : 
            ((Engineer) emp).getOvertime() * 100;
        double ctc = annualBase + allowance + specialPay;
        
        return String.format("""
            Employee Details:
            ----------------
            ID         : %s
            Name       : %s
            Department : %s
            Type       : %s
            Monthly    : ₹%.2f
            Annual     : ₹%.2f
            Allowance  : ₹%.2f
            %-9s: ₹%.2f
            CTC        : ₹%.2f
            """,
            emp.getId(),
            emp.getName(),
            emp.getDepartment(),
            emp.getClass().getSimpleName(),
            emp.salary,
            annualBase,
            allowance,
            (emp instanceof Manager) ? "Bonus" : "Overtime",
            specialPay,
            ctc);
    }

    private String formatEmployeeDetails(Employee emp) {
        double annualBase = emp.salary * 12;
        double allowance = annualBase * 0.1;
        double specialPay = (emp instanceof Manager) ? 
            ((Manager) emp).getBonus() : 
            ((Engineer) emp).getOvertime() * 100;
        double ctc = annualBase + allowance + specialPay;
        
        return String.format("""
            Employee Details:
            ----------------
            ID         : %s
            Name       : %s
            Department : %s
            Type       : %s
            Monthly    : ₹%.2f
            Annual     : ₹%.2f
            Allowance  : ₹%.2f
            %-9s: ₹%.2f
            CTC        : ₹%.2f
            """,
            emp.getId(),
            emp.getName(),
            emp.getDepartment(),
            emp.getClass().getSimpleName(),
            emp.salary,
            annualBase,
            allowance,
            (emp instanceof Manager) ? "Bonus" : "Overtime",
            specialPay,
            ctc);
    }

    private void loadForm(Employee emp) {
        nameField.setText(emp.getName());
        deptCombo.setValue(emp.getDepartment());
        salaryField.setText(String.valueOf(emp.salary));
        if (emp instanceof Manager mgr) {
            managerRadio.setSelected(true);
            extraField.setText(String.valueOf(mgr.getBonus()));
        } else {
            engineerRadio.setSelected(true);
            extraField.setText(String.valueOf(((Engineer) emp).getOvertime()));
        }
    }

    private void resetForm() {
        nameField.clear();
        salaryField.clear();
        extraField.clear();
        idField.clear();
        deptCombo.getSelectionModel().selectFirst();
        managerRadio.setSelected(true);
        currentLoadedEmployee = null;
        resultArea.clear();
    }
}
