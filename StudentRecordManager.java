import java.io.*;
import java.util.*;

class Student {
    String name;
    int rollNumber;
    String course;

    Student(String name, int rollNumber, String course) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.course = course;
    }

    public String toString() {
        return rollNumber + "," + name + "," + course;
    }

    public static Student fromString(String line) {
        String[] parts = line.split(",");
        return new Student(parts[1], Integer.parseInt(parts[0]), parts[2]);
    }
}

public class StudentRecordManager {
    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Student Record Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> updateStudent(sc);
                case 4 -> deleteStudent(sc);
                case 5 -> System.out.println("Exiting program. Goodbye!");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
    }

    static void addStudent(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Roll Number: ");
            int roll = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            Student s = new Student(name, roll, course);
            bw.write(s.toString());
            bw.newLine();
            System.out.println("Student added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    static void viewStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nRoll No\tName\tCourse");
            System.out.println("----------------------------");
            while ((line = br.readLine()) != null) {
                Student s = Student.fromString(line);
                System.out.println(s.rollNumber + "\t" + s.name + "\t" + s.course);
            }
        } catch (IOException e) {
            System.out.println("No records found.");
        }
    }

    static void updateStudent(Scanner sc) {
        try {
            System.out.print("Enter Roll Number to Update: ");
            int rollToUpdate = sc.nextInt();
            sc.nextLine();

            File inputFile = new File(FILE_NAME);
            File tempFile = new File("temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                Student s = Student.fromString(line);
                if (s.rollNumber == rollToUpdate) {
                    found = true;
                    System.out.print("Enter New Name: ");
                    s.name = sc.nextLine();
                    System.out.print("Enter New Course: ");
                    s.course = sc.nextLine();
                }
                bw.write(s.toString());
                bw.newLine();
            }

            br.close();
            bw.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                System.out.println(found ? "Record updated." : "Roll Number not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating file.");
        }
    }

    static void deleteStudent(Scanner sc) {
        try {
            System.out.print("Enter Roll Number to Delete: ");
            int rollToDelete = sc.nextInt();

            File inputFile = new File(FILE_NAME);
            File tempFile = new File("temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                Student s = Student.fromString(line);
                if (s.rollNumber == rollToDelete) {
                    found = true;
                    continue;
                }
                bw.write(s.toString());
                bw.newLine();
            }

            br.close();
            bw.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                System.out.println(found ? "Record deleted." : "Roll Number not found.");
            }

        } catch (IOException e) {
            System.out.println("Error deleting file.");
        }
    }
}
