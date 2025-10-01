//Superclass
class Person {
    String name;
    int age;

    // Constructor
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Method to display person info
    void displayPersonInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
    }
}

// Subclass
class Employee extends Person {
    String employeeId;
    String department;

    // Constructor
    Employee(String name, int age, String employeeId, String department) {
        super(name, age); // call superclass constructor
        this.employeeId = employeeId;
        this.department = department;
    }

    // Method to display employee info
    void displayEmployeeInfo() {
        super.displayPersonInfo(); // Call method from superclass
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Department: " + department);
    }
}

// Main class
public class InheritanceDemo {
    public static void main(String[] args) {
        Employee emp = new Employee("Shruti", 25, "E102", "HR");
        emp.displayEmployeeInfo();
    }
}
