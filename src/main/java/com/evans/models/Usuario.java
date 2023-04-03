package com.evans.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 3, max = 50, message = "El nombre debe de tener min. 3 caracteres y max. 50")
	private String nombre;
	
	@Column(name = "apellido_paterno")
	@Size(min = 3, max = 50, message = "El apellido paterno debe de tener min. 3 caracteres y max. 50")
	private String apellidoPaterno;
	
	@Column(name = "apellido_materno")
	@Size(min = 3, max = 50, message = "El apellido materno debe de tener min. 3 caracteres y max. 50")
	private String apellidoMaterno;

	@Email(message = "Debe proporcionar un email valido", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String username;

	@Size(min = 6, max = 50, message = "Debe proporcionar el password con un minimo de 6 caracteres")
	private String password;

	@Min(value = 0, message = "Cero es el valor minimo")
	@Max(value = 1, message = "Uno es el valor maximo")
	private Integer enabled = 0;

	public Usuario(Long id, @Email(message = "Debe proporcionar un email valido") String username,
			@Size(min = 8, max = 50, message = "Debe proporcionar un email con un minimo de 8 caracteres") String password,
			Integer enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public Usuario() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno
				+ ", apellidoMaterno=" + apellidoMaterno + ", username=" + username + ", password=" + password
				+ ", enabled=" + enabled + "]";
	}

}
