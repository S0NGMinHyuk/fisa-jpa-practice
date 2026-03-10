package fisa.jpa_practice;

import fisa.jpa_practice.model.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/{lectureId}")
    public Object register(@PathVariable int lectureId, @RequestParam int studentId) {

        try {
            Registration registration = registrationService.register(studentId, lectureId);

            return registration.getId();
        }catch (IllegalStateException e) {
            return e.getMessage();
        }
    }
}