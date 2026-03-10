package fisa.jpa_practice;

import fisa.jpa_practice.model.Lecture;
import fisa.jpa_practice.model.Registration;
import fisa.jpa_practice.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    @PersistenceContext
    private EntityManager manager;

    /**
     *
     * @param studentId
     * @param lectureId
     * @return
     */
    public Registration register(int studentId, int lectureId) {

        Student student = manager.find(Student.class, studentId);
        Lecture lecture = manager.find(Lecture.class, lectureId);

        Registration registration = Registration.builder()
                .student(student)
                .lecture(lecture)
                .build();

        manager.persist(registration);

        return registration;
    }
}