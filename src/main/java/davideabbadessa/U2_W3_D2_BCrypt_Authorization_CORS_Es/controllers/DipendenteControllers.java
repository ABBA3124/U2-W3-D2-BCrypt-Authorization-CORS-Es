package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.controllers;


import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dipendente")
public class DipendenteControllers {

    @Autowired
    private DipendenteService dipendenteService;

    @GetMapping
    public List<Dipendente> getAllDipendenti() {
        return dipendenteService.getAllDipendenti();
    }

    @GetMapping("/{id}")
    public Dipendente getDipendenteById(@PathVariable UUID id) {
        return dipendenteService.getDipendenteById(id);
    }


    @PutMapping("/{id}")
    public Dipendente updateDipendente(@PathVariable UUID id, @Validated @RequestBody DipendenteDTO dipendenteDTO) {
        return dipendenteService.updateDipendente(id, dipendenteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDipendente(@PathVariable UUID id) {
        dipendenteService.deleteDipendente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<Dipendente> uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Dipendente updatedDipendente = dipendenteService.uploadProfileImage(id, file);
            return ResponseEntity.ok(updatedDipendente);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }


}


