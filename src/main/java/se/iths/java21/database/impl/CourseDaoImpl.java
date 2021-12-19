package se.iths.java21.database.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import se.iths.java21.database.dao.CourseDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Teacher;

import java.sql.Date;
import java.util.List;

public class CourseDaoImpl implements CourseDao {
    EntityManagerFactory emf;
    EntityManager em;

    public CourseDaoImpl() {
        this.emf = Persistence.createEntityManagerFactory("FinalProject");
        this.em = emf.createEntityManager();
    }

    @Override
    public void insert(Course course) {
        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Course course) {
        em.getTransaction().begin();
        Program program = course.getProgram();
        program.getCourses().remove(course);
        em.merge(program);
        List<Teacher> teachers = course.getTeachers();
        teachers.forEach(teacher -> teacher.getCourses().remove(course));
        teachers.forEach(teacher -> em.merge(teacher));
        em.remove(course);
        em.getTransaction().commit();
    }

    @Override
    public void update(Course course) {
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
    }

    @Override
    public void truncate() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Course c").executeUpdate();
        em.createNativeQuery("ALTER TABLE Course AUTO_INCREMENT = 1").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public Course getByID(int id) {
        return em.find(Course.class, id);
    }

    @Override
    public List<Course> getByName(String name) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE name LIKE :name", Course.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Course> getAll() {
        return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }

    @Override
    public List<Course> getByStartDate(Date min, Date max) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE start_date BETWEEN :min AND :max", Course.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();    }

    @Override
    public List<Course> getByStartDate(Date exactDate) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE start_date = :exactDate", Course.class);
        query.setParameter("exactDate", exactDate);
        return query.getResultList();
    }

    @Override
    public List<Course> getByTeacher(Teacher teacher) {
        return teacher.getCourses().stream().toList();
    }

    @Override
    public List<Course> getByProgram(Program program) {
        return program.getCourses().stream().toList();
    }
}
