package sample.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import sample.model.Employee;
import sample.model.EmployeeDAO;
import sample.util.DBUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ONUR BASKIRT on 23.02.2016.
 */
public class EmployeeController {

    @FXML
    private TextField empIdText;
    @FXML
    private TextArea resultArea;
    @FXML
    private TextField newEmailText;
    @FXML
    private TextField nameText;
    @FXML
    private TextField surnameText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField occupationText;

    // editing Employee
    @FXML
    private TextField empNameT;
    @FXML
    private TextField empIdT;
    @FXML
    private TextField empJobT;
    @FXML
    private TextField empHireDateT;
    @FXML
    private TextField empSalaryT;
    // inserting Employee
    @FXML
    private TextField newEmpNameT;
    @FXML
    private TextField newEmpJobT;
    @FXML
    private TextField newEmpSalaryT;
    @FXML
    private TextField newEmpCommT;

    @FXML
    private TextField sqlQueryT;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> empIdColumn;
    @FXML
    private TableColumn<Employee, String> empNameColumn;
    @FXML
    private TableColumn<Employee, String> empJobColumn;
    @FXML
    private TableColumn<Employee, Double> empSalaryColumn;
    @FXML
    private TableColumn<Employee, Double> empCommColumn;
    @FXML
    private TableColumn<Employee, Date> empHireDateColumn;
    @FXML
    private TableColumn<Employee, Integer> empDeptNoColumn;


    private Employee selectedEmp;


    //Search an employee
    @FXML
    private void searchEmployee(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            //Get Employee information
            Employee emp = EmployeeDAO.searchEmployee(empIdText.getText());
            //Populate Employee on TableView and Display on TextArea
            populateAndShowEmployee(emp);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.clear();
            resultArea.setText("Error occurred while getting employee information from DB.\n" + e);
            throw e;
        }
    }

    //Search all employees
    @FXML
    private void searchEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            //Get all Employees information
            ObservableList<Employee> empData = EmployeeDAO.searchEmployees();
            //Populate Employees on TableView
            populateEmployees(empData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting employees information from DB.\n" + e);
            throw e;
        }
    }

    //Initializing the controller class.
    //This method is automatically called after the fxml file has been loaded.
    @FXML
    private void initialize() {

        /*
        The setCellValueFactory(...) that we set on the table columns are used to determine
        which field inside the Employee objects should be used for the particular column.
        The arrow -> indicates that we're using a Java 8 feature called Lambdas.
        (Another option would be to use a PropertyValueFactory, but this is not type-safe

        We're only using StringProperty values for our table columns in this example.
        When you want to use IntegerProperty or DoubleProperty, the setCellValueFactory(...)
        must have an additional asObject():
        */
        empIdColumn.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty().asObject());
        empNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        empJobColumn.setCellValueFactory(cellData -> cellData.getValue().jobIdProperty());
        empSalaryColumn.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());
        empCommColumn.setCellValueFactory(cellData -> cellData.getValue().commissionPctProperty().asObject());
        empHireDateColumn.setCellValueFactory(cellData -> cellData.getValue().hireDateProperty());
        empDeptNoColumn.setCellValueFactory(cellData -> cellData.getValue().departmentIdProperty().asObject());
        employeeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observable, Employee oldValue, Employee newValue) {
                if (employeeTable.getSelectionModel().getSelectedItem() != null) {
                    System.out.println(newValue);
                    empIdT.setText(String.valueOf(newValue.getEmployeeId()));
                    empNameT.setText(newValue.getFirstName());
                    empJobT.setText(newValue.getJobId());
                    empHireDateT.setText(newValue.getHireDate().toString());
                    empSalaryT.setText(String.valueOf(newValue.getSalary()));

                }
            }
        });

    }

    //Populate Employee
    @FXML
    private void populateEmployee(Employee emp) throws ClassNotFoundException {
        //Declare and ObservableList for table view
        ObservableList<Employee> empData = FXCollections.observableArrayList();
        //Add employee to the ObservableList
        empData.add(emp);
        //Set items to the employeeTable
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.setItems(empData);
    }

    //Set Employee information to Text Area
    @FXML
    private void setEmpInfoToTextArea(Employee emp) {
        resultArea.clear();
        resultArea.setText("First Name: " + emp.getFirstName() + "\n" +
                "Job: " + emp.getJobId());
    }

    //Populate Employee for TableView and Display Employee on TextArea
    @FXML
    private void populateAndShowEmployee(Employee emp) throws ClassNotFoundException {
        if (emp != null) {
            populateEmployee(emp);
            setEmpInfoToTextArea(emp);
        } else {
            resultArea.clear();
            resultArea.setText("This employee does not exist!\n");
        }
    }

    //Populate Employees for TableView
    @FXML
    private void populateEmployees(ObservableList<Employee> empData) throws ClassNotFoundException {

        //Set items to the employeeTable
        employeeTable.setItems(empData);
    }

    @FXML
    private void populateSqlResults(ObservableList empData) throws ClassNotFoundException {

        //Set items to the employeeTable
        employeeTable.setItems(empData);
    }

    //Update employee's email with the email which is written on newEmailText field
    @FXML
    private void updateEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            java.sql.Date sqlStartDate = null;
            SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY-MM-DD");
            try {
                java.util.Date date = sdf1.parse(empHireDateT.getText());
                sqlStartDate = new java.sql.Date(date.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(empHireDateT.getText());
            System.out.println(sqlStartDate != null ? sqlStartDate.toString() : null);
            selectedEmp = new Employee();
            selectedEmp.setEmployeeId(Integer.parseInt(empIdT.getText()));
            selectedEmp.setFirstName(empNameT.getText());
            selectedEmp.setJobId(empJobT.getText());
//            selectedEmp.setHireDate(sqlStartDate);
            selectedEmp.setSalary(Double.parseDouble(empSalaryT.getText()));
            EmployeeDAO.updateEmp(selectedEmp);
            resultArea.setText("Email has been updated for, employee id: " + selectedEmp.getFirstName() + "\n");
            updateTableView();
        } catch (SQLException e) {
            resultArea.clear();
            resultArea.setText("Problem occurred while updating email: " + e);
        }
    }

    //Insert an employee to the DB
    @FXML
    private void insertEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            Employee employee = new Employee();
            employee.setFirstName(newEmpNameT.getText());
            employee.setJobId(newEmpJobT.getText());
            employee.setSalary(Double.parseDouble(newEmpSalaryT.getText()));
            employee.setCommissionPct(Double.parseDouble(newEmpCommT.getText()));
            EmployeeDAO.insertEmp(employee);
            updateTableView();
            resultArea.clear();
            resultArea.setText("Employee inserted! \n");
        } catch (SQLException e) {
            resultArea.clear();
            resultArea.setText("Problem occurred while inserting employee " + e);
            throw e;
        }
    }

    //Delete an employee with a given employee Id from DB
    @FXML
    private void deleteEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            EmployeeDAO.deleteEmpWithId(empIdT.getText());
            resultArea.clear();
            resultArea.setText("Employee deleted! Employee id: " + empIdT.getText() + "\n");
            updateTableView();
        } catch (SQLException e) {
            resultArea.clear();
            resultArea.setText("Problem occurred while deleting employee " + e);
            throw e;
        }
    }

    private void updateTableView() throws ClassNotFoundException {
        try {
            //Get Employee information
            ObservableList<Employee> empData = EmployeeDAO.searchEmployees();
            //Populate Employee on TableView and Display on TextArea
            populateEmployees(empData);
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.clear();
            resultArea.setText("Error occurred while getting employee information from DB.\n" + e);
            try {
                throw e;
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

//    @FXML
//    private void executeQuery(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        try {
//            //Get all Employees information
//            ObservableList<ObservableList<String>> data
//                    = FXCollections.observableArrayList();
//            ResultSet resultSet = DBUtil.dbExecuteQuery(sqlQueryT.getText());
//            for (int i = 0; i < 4; i++) {
//                final int j = i;
//                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
//                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                        return new SimpleStringProperty(param.getValue().get(j).toString());
//                    }
//                });
//                employeeTable.getColumns().addAll(col);
//                System.out.println("Column [" + i + "] ");
//
//            }
//            while (resultSet.next()) {
//                ObservableList<String> row = FXCollections.observableArrayList();
//                for (int i = 0; i < 3; i++) {
//                    row.addAll(resultSet.getString(i + 1));
//                }
//                System.out.println("Row [1] added " + row);
//                data.addAll(row);
//            }
//
//            employeeTable.setItems(data);
////            populateSqlResults(data);
//            //Populate Employees on TableView
////            populateEmployees(empData);
//        } catch (SQLException e) {
//            System.out.println("Error occurred while getting employees information from DB.\n" + e);
//            throw e;
//        }
//    }

}
