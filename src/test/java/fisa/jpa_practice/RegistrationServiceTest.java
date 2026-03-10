package fisa.jpa_practice;

import fisa.jpa_practice.model.Lecture;
import fisa.jpa_practice.model.Registration;
import fisa.jpa_practice.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<Long> query;

    @Test
    @DisplayName("수강 신청 성공")
    void registerSuccess() {

        int studentId = 1;
        int lectureId = 1;

        Student student = new Student();
        Lecture lecture = new Lecture();

        // 학생 조회
        when(manager.find(Student.class, studentId)).thenReturn(student);

        // 강의 조회
        when(manager.find(Lecture.class, lectureId)).thenReturn(lecture);

        // 중복 체크 쿼리
        when(manager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(0L);

        Registration registration = registrationService.register(studentId, lectureId);

        assertNotNull(registration);
        verify(manager).persist(any(Registration.class));
    }

    @Test
    @DisplayName("중복 수강 신청 시 예외 발생")
    void registerDuplicate() {

        int studentId = 1;
        int lectureId = 1;

        Student student = new Student();
        Lecture lecture = new Lecture();

        when(manager.find(Student.class, studentId)).thenReturn(student);
        when(manager.find(Lecture.class, lectureId)).thenReturn(lecture);

        when(manager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        // 이미 수강신청 있음
        when(query.getSingleResult()).thenReturn(1L);

        assertThrows(IllegalStateException.class,
                () -> registrationService.register(studentId, lectureId));
    }
}