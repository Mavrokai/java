package com.myrestaurant.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Evite la page blanche HTTP 413 lors d'un upload trop volumineux.
 */
@ControllerAdvice
public class UploadExceptionHandler {

    private static final String MESSAGE =
            "Fichier trop volumineux (maximum 20 Mo). Compressez votre image ou GIF avant de l'envoyer.";

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException ex,
                                      HttpServletRequest request,
                                      RedirectAttributes ra) {
        ra.addFlashAttribute("uploadError", MESSAGE);
        if (request.getRequestURI().startsWith("/admin/dishes")) {
            return "redirect:/admin/dishes";
        }
        return "redirect:/setup";
    }
}
