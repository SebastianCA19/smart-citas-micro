package smartcitas.users.controller.dto;

public class UserDto {
    private int idUsuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private int estado;

    public UserDto(int idUsuario, String nombre, String primerApellido, String segundoApellido, String email, int estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public int getEstado() {
        return estado;
    }
}
