package columns;

import annotations.*;

@Entity
@Table(value = "laptops")
public class Laptops {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "laptopname")
    private String laptopName;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private Students students;

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLaptopName() {
        return laptopName;
    }

    public void setLaptopName(String laptopName) {
        this.laptopName = laptopName;
    }
}
