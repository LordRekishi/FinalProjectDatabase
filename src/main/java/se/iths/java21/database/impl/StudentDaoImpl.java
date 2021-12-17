package se.iths.java21.database.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import se.iths.java21.database.dao.StudentDao;
import se.iths.java21.database.entities.Program;
import se.iths.java21.database.entities.Student;

import java.sql.Date;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    EntityManagerFactory emf;
    EntityManager em;

    public StudentDaoImpl() {
        this.emf = Persistence.createEntityManagerFactory("FinalProject");
        this.em = emf.createEntityManager();
    }

    @Override
    public void insert(Student student) {
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Student student) {
        em.getTransaction().begin();
        student.getProgram().getStudents().remove(student);
        em.remove(student);
        em.getTransaction().commit();
    }

    @Override
    public void update(Student student) {
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    @Override
    public void truncate() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Student s").executeUpdate();
        em.createNativeQuery("ALTER TABLE Student AUTO_INCREMENT = 1").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public Student getByID(int id) {
        return em.find(Student.class, id);
    }

    @Override
    public List<Student> getByName(String name) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE CONCAT(first_name, ' ', last_name) LIKE :name", Student.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public List<Student> getAll() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    @Override
    public List<Student> getByDateOfBirth(Date min, Date max) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE date_of_birth BETWEEN :min AND :max", Student.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }

    @Override
    public List<Student> getByIsActive(boolean input) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE active = true", Student.class);
        return query.getResultList();
    }

    @Override
    public List<Student> getByProgram(Program program) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE program = :program", Student.class);
        query.setParameter("program", program);
        return query.getResultList();
    }
}
