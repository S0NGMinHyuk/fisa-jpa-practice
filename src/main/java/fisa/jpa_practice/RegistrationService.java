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

        // 중복 수강신청 체크
        Long count = manager.createQuery(
                        "select count(r) from Registration r " +
                                "where r.student.id = :studentId and r.lecture.id = :lectureId",
                        Long.class)
                .setParameter("studentId", studentId)
                .setParameter("lectureId", lectureId)
                .getSingleResult();

        if (count > 0) {
            throw new IllegalStateException("이미 수강 신청한 강의입니다.");
        }

        Registration registration = Registration.builder()
                .student(student)
                .lecture(lecture)
                .build();

        manager.persist(registration);

        return registration;
    }
}