package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.controllers;


import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.entities.Dipendente;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.payloads.DipendenteDTO;
import davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dipendente")
public class DipendenteControllers {

    @Autowired
    private DipendenteService dipendenteService;


    @GetMapping
    public Page<Dipendente> getAllDipendenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.getDipendenti(page, size, sortBy);
    }


    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser, @RequestBody DipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdateDipendente(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedUser) {
        this.dipendenteService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @GetMapping("/{userId}")
    public Dipendente findById(@PathVariable UUID userId) {
        return this.dipendenteService.getDipendenteById(userId);
    }

    //<--- ADMIN --->
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable UUID userId, @RequestBody DipendenteDTO dipendenteDTO) {
        return this.dipendenteService.findByIdAndUpdateDipendente(userId, dipendenteDTO);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID userId) {
        this.dipendenteService.findByIdAndDelete(userId);
    }

//    @GetMapping("/{id}")
//    public Dipendente getDipendenteById(@PathVariable UUID id) {
//        return dipendenteService.getDipendenteById(id);
//    }
//
//
//    @PutMapping("/{id}")
//    public Dipendente updateDipendente(@PathVariable UUID id, @Validated @RequestBody DipendenteDTO dipendenteDTO) {
//        return dipendenteService.updateDipendente(id, dipendenteDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteDipendente(@PathVariable UUID id) {
//        dipendenteService.deleteDipendente(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{id}/upload")
//    public ResponseEntity<Dipendente> uploadProfileImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
//        try {
//            Dipendente updatedDipendente = dipendenteService.uploadProfileImage(id, file);
//            return ResponseEntity.ok(updatedDipendente);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//

}


