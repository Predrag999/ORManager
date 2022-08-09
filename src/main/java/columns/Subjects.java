package columns;

import annotations.Column;
import annotations.Entity;
import annotations.Id;
import annotations.Table;


@Entity(name = "subjects")
@Table("subjects")
public class Subjects {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "subjectName")
    private String subjectName;

//    private Students students;

//    public Students getStudents() {
//        return students;
//    }

//    public void setStudents(Students students) {
//        this.students = students;
//    }


    //    private Grades grades;


    public Subjects(int id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }

    public Subjects() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

//    public Grades getGrades() {
//        return grades;
//    }
//
//    public void setGrades(Grades grades) {
//        this.grades = grades;
//    }
}
