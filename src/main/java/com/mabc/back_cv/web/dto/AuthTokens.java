package com.mabc.back_cv.web.dto;

/**
 * Registro (Record) que representa el par de tokens de autenticación JWT.
 * Contiene el token de acceso de corta duración y el token de refresco de larga duración.
 *
 * @param accessToken  Token de acceso utilizado para autorizar peticiones HTTP.
 * @param refreshToken Token de refresco utilizado para renovar el token de acceso expirado.
 */
public record AuthTokens(String accessToken, String refreshToken) {
}
