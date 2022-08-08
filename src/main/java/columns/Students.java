package columns;


import annotations.*;

import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;


//@Table("students")
@Entity(name = "students")
@Table("students")
public class Students {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "students")
    private List<Laptops> laptopsList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Laptops> getLaptopsList() {
        return laptopsList;
    }

    public void setLaptopsList(List<Laptops> laptopsList) {
        this.laptopsList = laptopsList;
    }

    //    @Column(name = "studentsSubjects")
//    @javax.persistence.OneToMany
//    private List<Subjects> list = new ArrayList<>();

//    public List<Subjects> getList() {
//        return list;
//    }
//
//    public void setList(List<Subjects> list) {
//        this.list = list;
//    }

    public int getStudentId() {
        return id;
    }

    public void setStudentId(int studentId) {
        this.id = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
