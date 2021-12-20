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
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.remove(course);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
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
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE Course").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
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
        return query.getResultList();
    }

    @Override
    public List<Course> getByStartDate(Date exactDate) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE start_date = :exactDate", Course.class);
        query.setParameter("exactDate", exactDate);
        return query.getResultList();
    }

    @Override
    public List<Course> getByProgram(Program program) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE program = :program", Course.class);
        query.setParameter("program", program);
        return query.getResultList();
    }

    @Override
    public List<Course> getByTeacher(Teacher teacher) {
        TypedQuery<Course> query = em.createQuery("SELECT c FROM Course c WHERE teachers = :teacher", Course.class);
        query.setParameter("teacher", teacher);
        return query.getResultList();
    }
}
