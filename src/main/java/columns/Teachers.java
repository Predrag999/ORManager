package columns;

import annotations.*;


@Entity
@Table("teachers")
public class Teachers {
    @Id
    @Column(name = "id")
    @JoinColumn
    private int id;
    @Column(name = "teacherFirstName")
    private String teacherFirstName;
    @Column(name = "teacherLastName")
    private String teacherLastName;
//    @OneToMany
//    @Column(name = "subjects")
//    private List<Subjects> subjects = new ArrayList<>();
    /////
//    private Grades grades;
//    private Students students;

    public int getTeacherId() {
        return id;
    }

    public void setTeacherId(int teacherId) {
        this.id = teacherId;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

//    public List<Subjects> getSubjects() {
//        return subjects;
//    }

//    public void setSubjects(List<Subjects> subjects) {
//        this.subjects = subjects;
//    }

//    public Grades getGrades() {
//        return grades;
//    }
//
//    public void setGrades(Grades grades) {
//        this.grades = grades;
//    }
//
//    public Students getStudents() {
//        return students;
//    }
//
//
//    public void setStudents(Students students) {
//        this.students = students;
//    }
}
