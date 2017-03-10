package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EmployeeDAO {

    //*******************************
    //SELECT an Employee
    //*******************************
    public static Employee searchEmployee(String empId) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM emp WHERE empno=" + empId;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            Employee employee = getEmployeeFromResultSet(rsEmp);
            //Return employee object
            return employee;
        } catch (SQLException e) {
            System.out.println("While searching an employee with " + empId + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }


    private static Employee getLastEmployee() {
        String selectStmt = "select  empno from emp where ROWNUM<=1 order by empno desc";
        Employee employee = null;
        try {
            ResultSet resultSet = DBUtil.dbExecuteQuery(selectStmt);
            if (resultSet.next()) {
                employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("EMPNO"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public static boolean checkExists(String id) {
        boolean isExists = false;
        String queryStm = "SELECT * FROM emp WHERE empno=" + id;
        try {
            ResultSet resultSet = DBUtil.dbExecuteQuery(queryStm);
            isExists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isExists;
    }

    //Use ResultSet from DB as parameter and set Employee Object's attributes and return employee object.
    private static Employee getEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee emp = null;
        if (rs.next()) {
            emp = new Employee();
            emp.setEmployeeId(rs.getInt("EMPNO"));
            emp.setFirstName(rs.getString("ENAME"));
            emp.setHireDate(rs.getDate("HIREDATE"));
            emp.setJobId(rs.getString("JOB"));
            emp.setCommissionPct(rs.getDouble("COMM"));
            emp.setDepartmantId(rs.getInt("DEPTNO"));
            emp.setSalary(rs.getInt("SAL"));
        }
        return emp;
    }

    //*******************************
    //SELECT Employees
    //*******************************
    public static ObservableList<Employee> searchEmployees() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM emp ORDER BY empno desc";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Employee> empList = getEmployeeList(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    //Select * from employees operation
    private static ObservableList<Employee> getEmployeeList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<Employee> empList = FXCollections.observableArrayList();

        while (rs.next()) {
            Employee emp = new Employee();
            emp.setEmployeeId(rs.getInt("EMPNO"));
            emp.setFirstName(rs.getString("ENAME"));
            emp.setHireDate(rs.getDate("HIREDATE"));
            emp.setJobId(rs.getString("JOB"));
            emp.setCommissionPct(rs.getDouble("COMM"));
            emp.setDepartmantId(rs.getInt("DEPTNO"));
            emp.setSalary(rs.getInt("SAL"));
            //Add employee to the ObservableList
            empList.add(emp);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    //*************************************
    //UPDATE an employee's email address
    //*************************************
    public static void updateEmp(Employee employee) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        System.out.println(employee.getFirstName());
        System.out.println(employee.getEmployeeId());
        System.out.println(employee.getHireDate());
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        System.out.println(dateFormat.format(employee.getHireDate()));
        String updateStmt =
                "BEGIN\n" +
                        "   UPDATE emp\n" +
                        "      SET ename = '" + employee.getFirstName() + "'\n" +
                        "      ,job = '" + employee.getJobId() + "'\n" +
                        "      ,sal = '" + String.valueOf(employee.getSalary()) + "'\n" +
//                        "      ,hiredate = '" + (employee.getHireDate()) + "'\n" +
                        "    WHERE empno = " + employee.getEmployeeId() + ";\n" +
                        "   COMMIT;\n" +
                        "END;";

        System.out.println(updateStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE an employee
    //*************************************
    public static void deleteEmpWithId(String empId) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                "BEGIN\n" +
                        "   DELETE FROM emp\n" +
                        "         WHERE empno =" + empId + ";\n" +
                        "   COMMIT;\n" +
                        "END;";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //INSERT an employee
    //*************************************
    public static void insertEmp(Employee employee) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement

        if (!checkExists(String.valueOf(employee.getEmployeeId()))) {
            int lastRecordId = getLastEmployee().getEmployeeId();
            String updateStmt =
                    "BEGIN\n" +
                            "INSERT INTO emp\n" +
                            "( empno ,ename, job,mgr,hiredate, sal,comm,deptno)\n" +
                            "VALUES\n" +
                            "(" + (lastRecordId + 1) + ",'" +
                            employee.getFirstName() + "','" +
                            employee.getJobId() + "'," +
                            null + "," +
                            null + "," +
                            employee.getSalary() + "," +
                            employee.getCommissionPct() + "," +
                            null + ");\n" +
                            "END;";

            //Execute DELETE operation
            try {
                DBUtil.dbExecuteUpdate(updateStmt);
            } catch (SQLException e) {
                System.out.print("Error occurred while DELETE Operation: " + e);
                throw e;
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Program Information");
            alert.setHeaderText("This user is " + employee.getFirstName() + " exist");
            alert.show();
        }
    }

}
