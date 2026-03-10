package fisa.jpa_practice;

import fisa.jpa_practice.model.Lecture;
import fisa.jpa_practice.model.Registration;
import fisa.jpa_practice.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<RegistrationDTO> findByStudentId(int studentId) {
        // 1. 엔티티 리스트 조회 (성능을 위해 fetch join 권장)
        List<Registration> registrations = manager.createQuery(
                        "select r from Registration r " +
                                "join fetch r.student s " +
                                "join fetch r.lecture l " +
                                "where s.id = :studentId", Registration.class)
                .setParameter("studentId", studentId)
                .getResultList();

        // 2. 엔티티 -> DTO 변환
        return registrations.stream()
                .map(r -> RegistrationDTO.builder()
                        .id(r.getId())
                        .studentName(r.getStudent().getName())
                        .lectureName(r.getLecture().getName())
                        .build())
                .collect(Collectors.toList());
    }
}