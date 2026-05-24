package com.mabc.back_cv.web.dto;

/**
 * Registro (Record) que encapsula la solicitud de renovación de token de acceso.
 * Contiene únicamente el token de refresco actual enviado por el cliente.
 *
 * @param refreshToken El token de refresco (refresh token) actual.
 */
public record RefreshTokenRequest(String refreshToken) {
}
