package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.BadRequestException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions.NotFoundException;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Dipendente> getAllDipendenti() {
        return dipendenteRepository.findAll();
    }

    public Page<Dipendente> getDipendenti(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return dipendenteRepository.findAll(pageable);
    }

    public Dipendente getDipendenteById(UUID id) {
        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente saveDipendente(DipendenteDTO dipendenteDTO) {
        if (dipendenteRepository.findByEmail(dipendenteDTO.email()).isEmpty()) {
            Dipendente dipendente = new Dipendente(dipendenteDTO.username(), dipendenteDTO.nome(), dipendenteDTO.cognome(), dipendenteDTO.email(), passwordEncoder.encode(dipendenteDTO.password()));
            dipendente.setAvatar("https://ui-avatars.com/api/?name=" + dipendente.getNome() + "+" + dipendente.getCognome());
            return dipendenteRepository.save(dipendente);
        } else {
            throw new BadRequestException("email già in uso!");
        }
    }

    public void deleteDipendente(UUID id) {
        dipendenteRepository.deleteById(id);
    }

    public Dipendente findByIdAndUpdateDipendente(UUID id, DipendenteDTO dipendenteDTO) {
        Dipendente dipendente = this.getDipendenteById(id);
        dipendente.setUsername(dipendenteDTO.username());
        dipendente.setNome(dipendenteDTO.nome());
        dipendente.setCognome(dipendenteDTO.cognome());
        dipendente.setEmail(dipendenteDTO.email());
        dipendente.setPassword(dipendenteDTO.password());
        dipendente.setAvatar("https://ui-avatars.com/api/?name=" + dipendenteDTO.nome() + "+" + dipendenteDTO.cognome());
        return dipendenteRepository.save(dipendente);
    }

    public Dipendente uploadProfileImage(UUID dipendenteId, MultipartFile file) throws IOException {
        Dipendente dipendente = this.getDipendenteById(dipendenteId);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadResult.get("url").toString();
        dipendente.setAvatar(imageUrl);

        return dipendenteRepository.save(dipendente);
    }

    public void findByIdAndDelete(UUID userId) {
        Dipendente found = this.getDipendenteById(userId);
        this.dipendenteRepository.delete(found);
    }


    public Dipendente findByEmail(String email) {
        return dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Dipendente con email " + email + "non trovato!"));
    }

}
