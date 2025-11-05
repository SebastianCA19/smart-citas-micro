package smartcitas.users.controller.dto;

public record UserDTORecord(int  idUsuario, String nombre, String primerApellido,
                            String segundoApellido, String email, int estado) {
}
