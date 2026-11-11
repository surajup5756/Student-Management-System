# WeIntern Pvt. Ltd. – Student Management System

A menu-driven console-based Student Management System developed using Java 11+ as part of the Week 2 Java Development Internship assignment.

The application allows users to manage student records efficiently through CRUD operations, input validation, file serialization, and student ID allocation management.

## Features

### Core Features

- **Add New Student:** Add student records with ID, name, age, GPA/grade, and subjects.
- **Display All Students:** Display all student records in a properly formatted table.
- **Search Student:** Search students by ID or full/partial name (case-insensitive).
- **Update Student Details:** Update student information with partial update support. Leave fields blank to retain existing values.
- **Delete Student:** Delete student records with a confirmation prompt.
- **Calculate Class Average:** Calculate and display the average GPA of all active students.
- **Data Persistence:** Automatically save and load student records using Java object serialization.

### Additional Features

- **Student ID Allocation Management:** Maintain a registry of all previously allocated student IDs.
- **Vacant ID Tracking:** Deleted student IDs are marked as VACANT and displayed separately.
- **Vacant ID Reuse:** Previously allocated vacant IDs can be reused when adding new students.
- **Duplicate ID Protection:** Prevent duplicate IDs and prevent reusing IDs that are already occupied.
- **Student Sorting:** Sort student records alphabetically by name.
- **CSV Export:** Export student records into a CSV file.
- **Input Validation:** Validate student IDs, names, ages, GPA values, and subject inputs.
- **Dashboard Summary:** Display total active students and class average GPA.
- **Automatic Saving:** Save data after successful student record modifications.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 11+ | Core programming language |
| OOP | Object-oriented application design |
| ArrayList | Store student records |
| HashMap | Fast student lookup by ID |
| TreeSet | Maintain allocated IDs in sorted order |
| Scanner | Handle user input |
| Java Serialization | Persistent data storage |
| File I/O | Read and write application data |
| CSV | Export student records |
| PlantUML | UML class diagram |

## Project Structure

```text
WeIntern_Student_Management_System/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weintern/
│                   └── sms/
│                       ├── Main.java
│                       ├── Student.java
│                       ├── StudentManager.java
│                       ├── StudentStore.java
│                       └── InputHelper.java
│
├── data/
│   └── .gitkeep
│
├── docs/
│   └── class-diagram.puml
│
├── out/
│   └── Compiled class files (generated automatically)
│
├── README.md
├── run.bat
└── run.sh
```

## Application Menu

The application provides the following menu options:

| Option | Function |
|---|---|
| 1 | Add New Student |
| 2 | Display All Students |
| 3 | Search Student (ID / Name) |
| 4 | Update Student Details |
| 5 | Delete Student |
| 6 | Calculate Class Average |
| 7 | Sort Students by Name |
| 8 | Export Students to CSV |
| 9 | View ID Allocation / Vacant IDs |
| 0 | Exit Application |

## Student ID Allocation and Vacant ID Management

The application maintains a record of all allocated student IDs.

- When a new student is added, their ID is registered.
- When a student is deleted, their personal and academic record is removed.
- The deleted student's ID remains in the allocation registry with VACANT status.
- Menu option 9 displays all allocated IDs with their ACTIVE or VACANT status.
- When adding a new student, the application provides an option to reuse a previously allocated vacant ID or enter a completely new ID.
- IDs currently assigned to active students cannot be reused.
- The ID registry is preserved even after restarting the application.

**Example:**

```text
STUDENT ID    STUDENT NAME       STATUS
---------------------------------------
1             Rahul Singh        ACTIVE
2             Rishi              ACTIVE
3             Jon                ACTIVE
104           -                  VACANT

Vacant IDs available for reuse: [104]
```

If ID 104 is vacant, a new student can be assigned the same ID.

## Data Storage and Persistence

Student records and allocated ID information are stored automatically using Java object serialization.

**Storage file:**

```text
data/students.dat
```

The `StudentStore` class maintains:
- Active student records.
- Previously allocated student IDs, including vacant IDs.

The application automatically loads saved data when it starts and saves updated information after successful modifications.

The serialized binary file should not be manually edited.

### CSV Export

Students can be exported using menu option 8.

Default output file:

```text
data/students.csv
```

The exported CSV file contains student information such as ID, name, age, GPA, and subjects.

## Input Validation

The application validates user inputs to maintain data consistency.

| Field | Validation |
|---|---|
| Student ID | 1–999999 |
| Student Name | Cannot be empty |
| Age | 5–100 |
| GPA / Grade | 0–10 |
| Subjects | Comma-separated values |
| Duplicate IDs | Not allowed |

Invalid inputs are handled with appropriate error messages.

## How to Run the Project

### Requirements

- Java Development Kit (JDK) 11 or later.
- VS Code, IntelliJ IDEA, or Eclipse.
- Windows, macOS, or Linux.

### Run on Windows

1. Extract the project ZIP file.
2. Open the `WeIntern_Student_Management_System` folder in VS Code.
3. Open the integrated terminal.
4. Execute:

```powershell
.\run.bat
```

The batch file automatically compiles the Java source files and launches the application.

### Run Manually on Windows PowerShell

From the project root directory:

```powershell
if (!(Test-Path out)) {
    New-Item -ItemType Directory out | Out-Null
}

javac -encoding UTF-8 -d out src\main\java\com\weintern\sms\*.java

java -cp out com.weintern.sms.Main
```

### Run on macOS / Linux

Execute:

```bash
chmod +x run.sh
./run.sh
```

## Suggested Test Cases

1. Add a student with valid information.
2. Attempt to add a duplicate student ID.
3. Display all active student records.
4. Search for a student using their ID.
5. Search using a partial name with different letter casing.
6. Search for a non-existing student.
7. Update only the student's GPA.
8. Update student subjects.
9. Delete a student and confirm with `n`.
10. Delete a student and confirm with `y`.
11. Enter invalid age or GPA values.
12. Enter a GPA below 0 or above 10.
13. Calculate the class average.
14. Sort student records alphabetically.
15. Export student records to CSV.
16. View student ID allocation status.
17. Delete a student and verify that their ID becomes vacant.
18. Reuse a previously allocated vacant ID.
19. Restart the application and verify data persistence.

## UML Class Diagram

The project includes a UML class diagram created using PlantUML.

Location:

```text
docs/class-diagram.puml
```

The diagram represents the application's main classes and their relationships:

- Student
- StudentManager
- StudentStore
- InputHelper
- Main

## Learning Outcomes

This project demonstrates practical implementation of:

- Object-Oriented Programming (OOP).
- Classes, objects, constructors, and encapsulation.
- Java Collections Framework.
- ArrayList, HashMap, and TreeSet.
- CRUD operations.
- Exception handling and input validation.
- File handling and object serialization.
- Data management and ID allocation.
- Modular Java application development.

## Author

Developed as part of the WeIntern Pvt. Ltd. Java Development Internship – Week 2 Assignment.

**Project:** Student Management System  
**Language:** Java  
**Application Type:** Console-Based Application  
**Java Version:** 11+