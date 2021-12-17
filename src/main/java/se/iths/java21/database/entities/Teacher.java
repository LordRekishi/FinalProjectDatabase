package se.iths.java21.database.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;
    private String last_name;
    private Date start_of_employment;

    @ManyToMany(targetEntity = Course.class)
    private List<Course> courses = new ArrayList<>();

    public Teacher() {}

    public Teacher(String first_name, String last_name, Date start_of_employment) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.start_of_employment = start_of_employment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getStart_of_employment() {
        return start_of_employment;
    }

    public void setStart_of_employment(Date start_of_employment) {
        this.start_of_employment = start_of_employment;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.getTeachers().add(this);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", start_of_employment=" + start_of_employment +
                ", courses=" + courses +
                '}';
    }
}
