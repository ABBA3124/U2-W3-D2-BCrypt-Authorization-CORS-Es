package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services;

import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.UnauthorizedException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteLoginDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authDipendenteAndGenerateToken(DipendenteLoginDTO payload) {
        Dipendente dipendente = this.dipendenteService.findByEmail(payload.email());

        if (passwordEncoder.matches(payload.password(), dipendente.getPassword())) {
            return jwtTools.creaToken(dipendente);
        } else {
            throw new UnauthorizedException("Credenziali non corrette!");
        }
    }
}
