package ar.com.wolox.model.socialmedia.common;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {
        INVALID_USER_ID_VALUE("El ID del usuario no puede ser nulo o vacio"),
        INVALID_GRANT_VALUE("El valor de 'grant' no puede ser nulo o vacio"),
        INVALID_ALBUM_VALUE("El ID del album no puede ser nulo o vacio"),
        SHARED_ALBUM_INVALID_DATA("La información del nuevo album compartido es obligatoria"),
        SHARED_ALBUM_USER_GUEST_ERROR("No puede compartir un album consigo mismo"),
        SHARED_ALBUM_NO_GRANT_ALBUM_GUEST("El álbum no está compartido con el usuario"),
        SHARED_ALBUM_UPDATE_GRANT_INVALID_DATA_ERROR("Faltan datos para actualizar el album");

        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build() {
            return new BusinessException(HttpStatus.BAD_REQUEST, this);
        }

        public Supplier<Throwable> defer() {
            return () -> new BusinessException(HttpStatus.BAD_REQUEST, this);
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessException(HttpStatus status, Type type){
        super(status, type.message);
        this.type = type;
    }

    @Override
    public String getCode(){
        return type.name();
    }


}
