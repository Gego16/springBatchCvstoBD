package com.api.web.model;



import java.sql.Date;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Estudiante {
	
	@Id
	@Column(name="id")
	Long id;
	
	@NotBlank
	@Column(name="nombre")
	String nombre;
	
	@NotBlank
	@Column(name="apMaterno")
	String apMaterno;
	
	@NotBlank
	@Column(name="apPaterno")
	String apPaterno;
	
	@NotBlank
	@Email
	@Column(name="correo")
	String correo;
	
	
	@Column(name="edad")
	int edad;
	
	
	public static String[] fields() {
		return new String[] {"id","nombre","apPaterno","apMaterno","correo","edad"};
	}
}
