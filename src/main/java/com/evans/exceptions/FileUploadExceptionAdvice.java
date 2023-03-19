package com.evans.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {

	/**************************************************************************************************
	 * * Manejamos la excepcion si el archivo que se intenta guardar excede el
	 * tamanio permitido
	 **************************************************************************************************/
	@ExceptionHandler({ MaxUploadSizeExceededException.class })
	public ResponseEntity<?> handleMaxUploadSizeExceededException() {
		Map<String, Object> response = new HashMap<>();
		response.put("msg", "La imagen es mas pesada de lo esperado, Tam. Max: 2MB");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

}
