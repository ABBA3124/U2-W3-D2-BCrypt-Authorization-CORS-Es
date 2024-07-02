package davideabbadessa.U2_W3_D2_BCrypt_Authorization_CORS_Es.exceptions;


import java.util.UUID;


public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("ID: --> " + id + " non trovato!");
    }

    public NotFoundException(String messaggio) {
        super(messaggio);
    }


}
