package columns;

import annotations.*;

@Entity
@Table(value = "laptops")
public class Laptops {

    @Id
    @Column(name = "laptopid")
    private int id;

    @Column(name = "laptopname")
    private String laptopName;


    @ManyToOne(name = "studentId")
    @JoinColumn(name = "studentId")
    private Students studentsId;


    public Students getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(Students studentsId) {
        this.studentsId = studentsId;
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
