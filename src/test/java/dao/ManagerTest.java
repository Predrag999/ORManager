package dao;

import columns.Students;
import columns.Subjects;
import columns.Teachers;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ManagerTest {

    static Manager manager = new Manager();

    static {
        manager.withPropertiesFrom("persistence.properties");
    }

    @Test
    @DisplayName("Test1")
    public void test1() {
        Subjects firstSubject = new Subjects(2021, "First Subject");
        Subjects secondSubject = new Subjects(2022, "Second Subject");
        Subjects thirdSubject = new Subjects(2023, "Third Subject");

        manager.register(Subjects.class);
        manager.save(firstSubject);
        manager.save(secondSubject);
        manager.save(thirdSubject);

        firstSubject.setSubjectName("New First Subject");
        secondSubject.setSubjectName("New Second Subject");
        thirdSubject.setSubjectName("New Third Subject");
        manager.merge(firstSubject);
        manager.merge(secondSubject);
        manager.merge(thirdSubject);

        assertEquals("New First Subject", firstSubject.getSubjectName());
        assertEquals("New Second Subject", secondSubject.getSubjectName());
        assertEquals("New Third Subject", thirdSubject.getSubjectName());

    }

    @Test
    @DisplayName("Test2")
    public void test2() throws Exception {

        Subjects math = new Subjects();
        math.setId(1);
        math.setSubjectName("Math");

        manager.save(math);

        manager.delete(math);

        Subjects subjects = math.getClass().getConstructor().newInstance();

        assertEquals(Optional.empty(), manager.findByID(math.getId(), Subjects.class));

    }

    @Test
    @DisplayName("Test3")
    public void test3() throws Exception {

        Subjects astrology = new Subjects();
        astrology.setId(1689);
        astrology.setSubjectName("Astrology");

        manager.save(astrology);

        manager.delete(astrology);

        Subjects subjects = astrology.getClass().getConstructor().newInstance();

        assertEquals(Optional.empty(), manager.findByID(astrology.getId(), Subjects.class));

    }

    @Test
    @DisplayName("Test4")
    public void test4() {
        Teachers firstTeacher = new Teachers();

        firstTeacher.setTeacherId(1);
        firstTeacher.setTeacherFirstName("Name");
        firstTeacher.setTeacherLastName("Surname");

        manager.register(Teachers.class);
        manager.save(firstTeacher);

        firstTeacher.setTeacherFirstName("John");
        manager.merge(firstTeacher);

        assertEquals("John", firstTeacher.getTeacherFirstName());

    }

    @Test
    @DisplayName("Test5")
    public void test5() throws Exception {
        Students mark = new Students();
        mark.setId(1);
        mark.setFirstName("Mark");
        mark.setLastName("Zuckerberg");

        manager.register(Students.class);
        manager.save(mark);

        manager.delete(mark);

        assertEquals(Optional.empty(), manager.findByID(mark.getId(), Students.class));

    }

    @Test
    @DisplayName("Test6")
    public void test6() throws Exception {
        Teachers teacher1 = new Teachers();
        Teachers teacher2 = new Teachers();
        Teachers teacher3 = new Teachers();

        manager.dropTable(Teachers.class);

        List<Object> list = List.of(); // an empty list just to be sure

        assertEquals(list, manager.findAll(Teachers.class));
    }

    @Test
    @DisplayName("Test7")
    public void test7() throws Exception {
        Students max = new Students();
        Students daniel = new Students();
        Students gerard = new Students();

        max.setId(1000);
        daniel.setId(1001);
        gerard.setId(1002);

        max.setFirstName("Max");
        daniel.setFirstName("Daniel");
        gerard.setFirstName("Gerard");

        max.setLastName("Maximov");
        daniel.setLastName("Danielov");
        gerard.setLastName("Gerardov");

        manager.save(max);
        manager.save(daniel);
        manager.save(gerard);

        manager.dropTable(Students.class);

        List<Object> emptyList = new ArrayList<>();

        assertEquals(emptyList, manager.findAll(Students.class));
    }

    @Test
    @DisplayName("test8")
    public void test8() throws Exception{
        Students john = new Students();
        john.setId(1);
        john.setFirstName("John");
        john.setLastName("Sunderland");

        Students gregor = new Students();
        gregor.setId(2);
        gregor.setFirstName("Gregor");
        gregor.setLastName("Anderson");

        manager.register(Students.class);
        manager.save(john);
        manager.save(gregor);

        var updateForJohn = manager.update(john);
        var updateForGregor = manager.update(gregor);

        assertEquals(1,updateForJohn.getId());
        assertEquals(2,updateForGregor.getId());

    }
    @Test
    @DisplayName("test9")
    public void test9() throws Exception {

    }

}