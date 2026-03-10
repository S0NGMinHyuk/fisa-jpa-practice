package fisa.jpa_practice;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class RegistrationDTO {
    private int id;
    private String studentName;
    private String lectureName;
}
