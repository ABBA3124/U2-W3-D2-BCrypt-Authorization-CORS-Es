package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.controllers;

import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.BadRequestException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteLoginDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteLoginResponseDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteResponseDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services.AuthService;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public DipendenteLoginResponseDTO login(@RequestBody DipendenteLoginDTO payload) {
        return new DipendenteLoginResponseDTO(authService.authDipendenteAndGenerateToken(payload));
    }


    @PostMapping("/registrati")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteResponseDTO createDipendente(@Validated @RequestBody DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            System.out.println(validationResult.getAllErrors());
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new DipendenteResponseDTO(this.dipendenteService.saveDipendente(body).getId());
    }

}
