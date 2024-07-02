package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.security;

import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.UnauthorizedException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private DipendenteService dipendenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Per favore inserisci correttamente il token nell'header");

        String accessToken = authHeader.substring(7);

        jwtTools.verifyToken(accessToken);

        // 4. Se tutto è OK, proseguiamo con il prossimo elemento della Filter Chain, prima o poi arriveremo all'endpoint
        // Se vogliamo abilitare l'autorizzazione dobbiamo 'informare' Spring Security di chi sia l'utente che sta effettuando la richiesta

        // 4.1 Cerco l'utente tramite id (l'id sta nel token..)
        String userId = jwtTools.extractIdFromToken(accessToken);
        Dipendente currentUser = dipendenteService.getDipendenteById(UUID.fromString(userId));

        // 4.2 Trovato l'utente posso associarlo al Security Context, praticamente questo equivale ad 'associare' l'utente autenticato alla richiesta corrente
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        // Il terzo parametro è OBBLIGATORIO se si vuol poter usare i vari @PreAuthorize perché esso contiene la lista dei ruoli dell'utente
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
