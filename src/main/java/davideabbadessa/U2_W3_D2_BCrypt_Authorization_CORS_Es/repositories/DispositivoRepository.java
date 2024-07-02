package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.repositories;

import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dispositivi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivi, UUID> {
}
