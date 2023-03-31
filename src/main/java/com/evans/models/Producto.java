package com.evans.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "productos")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre del producto es obligatorio")
	private String nombre;

	@NotBlank(message = "El la descripcion del producto es obligatorio")
	private String descripcion;

	@Min(value = 0, message = "El precio minimo del producto es 0")
	// @NotBlank(message = "El precio del producto es obligatorio")
	private Double precio;

	@Min(value = 0, message = "El minimo de stock del producto debe ser 0")
	private Integer stock;

	@Column(name = "created_at")
	private LocalDate createdAt;

	private String image = "no-image.png";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id")
	@NotNull(message = "La categoria del producto es obligatorio")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Categoria categoria;

	public Producto() {

	}

	public Producto(Long id, @NotBlank(message = "El nombre del producto es obligatorio") String nombre,
			@NotBlank(message = "El la descripcion del producto es obligatorio") String descripcion,
			@Min(value = 0, message = "El precio minimo del producto es 0") Double precio,
			@Min(value = 0, message = "El minimo de stock del producto debe ser 0") Integer stock, LocalDate createdAt,
			String image, @NotNull(message = "La categoria del producto es obligatorio") Categoria categoria) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.createdAt = createdAt;
		this.image = image;
		this.categoria = categoria;
	}

	@PrePersist
	private void generateCreatedAt() {
		createdAt = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", stock=" + stock + ", createdAt=" + createdAt + ", image=" + image + ", categoria=" + categoria
				+ "]";
	}

}
