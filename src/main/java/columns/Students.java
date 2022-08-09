package columns;


import annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "students")
@Table("students")
public class Students {
    @Id
    @Column(name = "student_Id")
    private int id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;

    @OneToMany(mappedBy = "students")
    private List<Laptops> laptopsList = new ArrayList<>() {
    };

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
