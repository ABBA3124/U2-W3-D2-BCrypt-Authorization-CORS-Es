package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services;


import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dispositivi;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.enums.StatoDispositivo;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.enums.TipoDispositivo;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.BadRequestException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.NotFoundException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DispositivoDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.repositories.DipendenteRepository;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.repositories.DispositivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DispositivoService {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;


    public List<Dispositivi> getAllDispositivi() {
        return dispositivoRepository.findAll();
    }

    public Optional<Dispositivi> getDispositivoById(UUID id) {
        return dispositivoRepository.findById(id);
    }

    public Dispositivi saveDispositivo(DispositivoDTO dispositivoDTO) {
        Dispositivi dispositivi = new Dispositivi();
        dispositivi.setTipo(TipoDispositivo.valueOf(dispositivoDTO.tipo().toUpperCase()));
        dispositivi.setStato(StatoDispositivo.valueOf(dispositivoDTO.stato().toUpperCase()));

        if (dispositivoDTO.dipendenteId() != null) {
            Dipendente dipendente = dipendenteRepository.findById(dispositivoDTO.dipendenteId())
                    .orElseThrow(() -> new NotFoundException(dispositivoDTO.dipendenteId()));
            dispositivi.setDipendente(dipendente);
        }

        return dispositivoRepository.save(dispositivi);
    }


    public Dispositivi updateDispositivo(UUID id, DispositivoDTO dispositivoDTO) {
        Dispositivi dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        dispositivo.setTipo(TipoDispositivo.valueOf(dispositivoDTO.tipo().toUpperCase()));
        dispositivo.setStato(StatoDispositivo.valueOf(dispositivoDTO.stato().toUpperCase()));

        if (dispositivoDTO.dipendenteId() != null) {
            Dipendente dipendente = dipendenteRepository.findById(dispositivoDTO.dipendenteId())
                    .orElseThrow(() -> new NotFoundException(dispositivoDTO.dipendenteId()));
            dispositivo.setDipendente(dipendente);
        } else {
            dispositivo.setDipendente(null);
        }

        return dispositivoRepository.save(dispositivo);
    }


    public void deleteDispositivo(UUID id) {
        dispositivoRepository.deleteById(id);
    }

    public Dispositivi assignDispositivo(UUID dispositivoId, UUID dipendenteId) {
        Dispositivi dispositivo = dispositivoRepository.findById(dispositivoId)
                .orElseThrow(() -> new NotFoundException(dispositivoId));
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new NotFoundException(dipendenteId));

        if (dispositivo.getStato() != StatoDispositivo.DISPONIBILE) {
            throw new BadRequestException("Il dispositivo non è disponibile per l'assegnazione.");
        }

        dispositivo.setDipendente(dipendente);
        dispositivo.setStato(StatoDispositivo.ASSEGNATO);

        return dispositivoRepository.save(dispositivo);
    }

}
