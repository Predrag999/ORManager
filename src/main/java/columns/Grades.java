package columns;

import annotations.Id;

@annotations.Entity
public class Grades {
    @Id
    private int gradeId;

    @annotations.Column
    private String gradeName;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
