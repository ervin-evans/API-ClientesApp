package com.evans.helpers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UploadPhoto {
	public static String savePhoto(MultipartFile multipartFile, String path) {
		String originalName = multipartFile.getOriginalFilename();
		originalName = originalName.replace(" ", "");
		originalName = String.valueOf(UUID.randomUUID() + "-" + originalName);
		try {
			File imageFile = new File(path + originalName);
			// guardamos fisicamente la imagen el el disco
			multipartFile.transferTo(imageFile);
			return originalName;
		} catch (IOException e) {
			System.err.println("Error al guardar la imagen en el servidor");
			return null;
		}
	}

	public static boolean isValidImageExtension(String imageName) {
		if (imageName.endsWith(".PNG") || imageName.endsWith(".JPG")
				|| imageName.endsWith(".JPEG") ||imageName.endsWith(".png") || imageName.endsWith(".jpg")
						|| imageName.endsWith(".jpeg"))
			return true;
		return false;
	}
}
