package se.iths.java21.database.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import se.iths.java21.database.dao.ProgramDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;

import java.sql.Date;
import java.util.List;

public class ProgramDaoImpl implements ProgramDao {
    EntityManagerFactory emf;
    EntityManager em;

    public ProgramDaoImpl() {
        this.emf = Persistence.createEntityManagerFactory("FinalProject");
        this.em = emf.createEntityManager();
    }

    @Override
    public void insert(Program program) {
        em.getTransaction().begin();
        em.persist(program);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Program program) {
        em.getTransaction().begin();
        List<Course> courses = program.getCourses();
        courses.forEach(course -> course.setProgram(null));
        courses.forEach(course -> em.merge(course));
        List<Student> students = program.getStudents();
        students.forEach(student -> student.setProgram(null));
        students.forEach(student -> em.merge(student));
        em.remove(program);
        em.getTransaction().commit();
    }

    @Override
    public void update(Program program) {
        em.getTransaction().begin();
        em.merge(program);
        em.getTransaction().commit();
    }

    @Override
    public void truncate() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Program p").executeUpdate();
        em.createNativeQuery("ALTER TABLE Program AUTO_INCREMENT = 1").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public Program getByID(int id) {
        return em.find(Program.class, id);
    }

    @Override
    public List<Program> getByName(String name) {
        TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p WHERE name LIKE :name", Program.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Program> getAll() {
        return em.createQuery("SELECT p FROM Program p", Program.class).getResultList();
    }

    @Override
    public List<Program> getByStartDate(Date min, Date max) {
        TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p WHERE start_date BETWEEN :min AND :max", Program.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }

    @Override
    public List<Program> getByStartDate(Date exactDate) {
        TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p WHERE start_date = :exactDate", Program.class);
        query.setParameter("exactDate", exactDate);
        return query.getResultList();
    }

    @Override
    public Program getByCourse(Course course) {
        return course.getProgram();
    }

    @Override
    public Program getByStudent(Student student) {
        return student.getProgram();
    }
}
