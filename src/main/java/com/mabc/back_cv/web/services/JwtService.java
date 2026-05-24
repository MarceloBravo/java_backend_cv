package com.mabc.back_cv.web.services;

import com.mabc.back_cv.web.dto.AuthTokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Servicio encargado de la generación, extracción y validación de tokens JWT (JSON Web Tokens).
 * Proporciona soporte tanto para tokens de acceso (Access Tokens) como para tokens de refresco (Refresh Tokens).
 */
@Service
public class JwtService {

    /**
     * Clave secreta para firmar los tokens JWT. Debe ser lo suficientemente larga y segura en producción.
     */
    private static final String SECRET_KEY = "tu_llave_secreta_super_segura_y_larga_para_generar_el_jwt";

    /**
     * Nombre de la propiedad del token que indica su tipo.
     */
    private static final String TOKEN_TYPE_CLAIM = "type";

    /**
     * Valor del tipo para tokens de acceso.
     */
    private static final String ACCESS_TOKEN_TYPE = "access";

    /**
     * Valor del tipo para tokens de refresco.
     */
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    /**
     * Tiempo de expiración del token de acceso (15 minutos).
     */
    private static final long ACCESS_TOKEN_EXPIRATION_MS = 1000L * 60 * 15; // 15 minutos

    /**
     * Tiempo de expiración del token de refresco (7 días).
     */
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; // 7 días

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario (email) contenido en el token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Genera un par de tokens (acceso y refresco) para un usuario.
     *
     * @param userDetails Detalles del usuario para el cual se generan los tokens.
     * @return Un objeto {@link AuthTokens} con el token de acceso y de refresco.
     */
    public AuthTokens generateTokenPair(UserDetails userDetails) {
        return new AuthTokens(
                generateAccessToken(userDetails),
                generateRefreshToken(userDetails)
        );
    }

    /**
     * Genera un token de acceso de corta duración.
     *
     * @param userDetails Detalles del usuario.
     * @return El token de acceso generado como cadena.
     */
    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, ACCESS_TOKEN_TYPE, ACCESS_TOKEN_EXPIRATION_MS);
    }

    /**
     * Genera un token de refresco de larga duración.
     *
     * @param userDetails Detalles del usuario.
     * @return El token de refresco generado como cadena.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, REFRESH_TOKEN_TYPE, REFRESH_TOKEN_EXPIRATION_MS);
    }

    /**
     * Comprueba si el token dado es un token de acceso.
     *
     * @param token El token a comprobar.
     * @return true si es de acceso, false en caso contrario.
     */
    public boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(extractTokenType(token));
    }

    /**
     * Comprueba si el token dado es un token de refresco.
     *
     * @param token El token a comprobar.
     * @return true si es de refresco, false en caso contrario.
     */
    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(extractTokenType(token));
    }

    /**
     * Verifica si un token de acceso es válido y corresponde al usuario.
     *
     * @param token       El token de acceso.
     * @param userDetails Detalles del usuario.
     * @return true si es de tipo acceso, no ha expirado y coincide el usuario; false de lo contrario.
     */
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isAccessToken(token) && isTokenValid(token, userDetails);
    }

    /**
     * Verifica si un token de refresco es válido y corresponde al usuario.
     *
     * @param token       El token de refresco.
     * @param userDetails Detalles del usuario.
     * @return true si es de tipo refresco, no ha expirado y coincide el usuario; false de lo contrario.
     */
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return isRefreshToken(token) && isTokenValid(token, userDetails);
    }

    /**
     * Verifica si las credenciales del token son válidas (coincide el usuario y no ha expirado).
     *
     * @param token       El token JWT.
     * @param userDetails Detalles del usuario.
     * @return true si es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private String buildToken(UserDetails userDetails, String tokenType, long expirationMs) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
