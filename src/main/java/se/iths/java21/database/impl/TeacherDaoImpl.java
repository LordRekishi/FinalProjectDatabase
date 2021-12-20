package se.iths.java21.database.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import se.iths.java21.database.dao.TeacherDao;
import se.iths.java21.database.entities.Course;
import se.iths.java21.database.entities.Teacher;

import java.sql.Date;
import java.util.List;

public class TeacherDaoImpl implements TeacherDao {
    EntityManagerFactory emf;
    EntityManager em;

    public TeacherDaoImpl() {
        this.emf = Persistence.createEntityManagerFactory("FinalProject");
        this.em = emf.createEntityManager();
    }

    @Override
    public void insert(Teacher teacher) {
        em.getTransaction().begin();
        em.persist(teacher);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Teacher teacher) {
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.remove(teacher);
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        em.getTransaction().commit();

    }

    @Override
    public void update(Teacher teacher) {
        em.getTransaction().begin();
        em.merge(teacher);
        em.getTransaction().commit();
    }

    @Override
    public void truncate() {
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE Teacher").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE course_teacher").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public Teacher getByID(int id) {
        return em.find(Teacher.class, id);
    }

    @Override
    public List<Teacher> getByName(String name) {
        TypedQuery<Teacher> query = em.createQuery("SELECT t FROM Teacher t WHERE CONCAT(first_name, ' ', last_name) LIKE :name", Teacher.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public List<Teacher> getAll() {
        return em.createQuery("SELECT t FROM Teacher t", Teacher.class).getResultList();
    }

    @Override
    public List<Teacher> getByStartOfEmployment(Date min, Date max) {
        TypedQuery<Teacher> query = em.createQuery("SELECT t FROM Teacher t WHERE start_of_employment BETWEEN :min AND :max", Teacher.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }

    @Override
    public List<Teacher> getByCourse(Course course) {
        return course.getTeachers();
    }
}
