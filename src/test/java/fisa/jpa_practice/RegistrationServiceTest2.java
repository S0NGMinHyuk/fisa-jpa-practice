package fisa.jpa_practice;

import static org.assertj.core.api.Assertions.assertThat;

import fisa.jpa_practice.model.Lecture;
import fisa.jpa_practice.model.Major;
import fisa.jpa_practice.model.Professor;
import fisa.jpa_practice.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional // 테스트 완료 후 데이터를 롤백하여 DB를 깨끗하게 유지합니다.
class RegistrationServiceTest2 {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("학생 ID로 수강신청 내역을 조회하면 DTO 리스트가 반환되어야 한다")
    void findByStudentIdTest() {
        // 1. Given: 테스트 데이터 준비 (Major -> Student, Professor -> Lecture -> Registration)
        Major major = Major.builder()
                .name("캐릭터학부")
                .build();
        em.persist(major);

        Student student = Student.builder()
                .name("가나디")
                .major(major)
                .build();
        em.persist(student);

        Professor professor = Professor.builder()
                .name("웹툰작가")
                .major(major)
                .build();
        em.persist(professor);

        Lecture lecture = Lecture.builder()
                .name("캐릭터 그리기")
                .professor(professor)
                .major(major)
                .build();
        em.persist(lecture);

        // 실제 수강신청 데이터 생성
        registrationService.register(student.getId(), lecture.getId());

        // 영속성 컨텍스트 초기화 (DB에서 새로 읽어오는지 확인하기 위함)
        em.flush();
        em.clear();

        // 2. When: 조회 실행
        List<RegistrationDTO> result = registrationService.findByStudentId(student.getId());

        // 3. Then: 검증
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getStudentName()).isEqualTo("가나디");
        assertThat(result.get(0).getLectureName()).isEqualTo("캐릭터 그리기");

        // 로그로 확인
        System.out.println("결과 DTO: " + result.get(0));
    }
}