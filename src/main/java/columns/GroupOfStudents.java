package columns;

import annotations.Column;
import annotations.Entity;
import annotations.Id;


import java.util.ArrayList;
import java.util.List;

@Entity
//@Table("groupofstudents")
public class GroupOfStudents {
    @Id
    @Column
    private int groupId;

    //    @JoinColumn(name ="studentId", nullable = false)
    @Column
    private String groupName;

//    @ManyToMany
    @Column
    private List<Students> group = new ArrayList<>();
//    @ManyToMany
    @Column
    private List<Subjects> subjects = new ArrayList<>();

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Students> getGroup() {
        return group;
    }

    public void setGroup(List<Students> group) {
        this.group = group;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subjects> subjects) {
        this.subjects = subjects;
    }
}
