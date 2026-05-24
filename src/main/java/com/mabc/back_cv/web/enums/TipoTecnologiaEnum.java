package com.mabc.back_cv.web.enums;

/**
 * Enumerado que define los diferentes tipos de tecnologías utilizadas en el sistema
 * (ej. en el perfil o currículum de los usuarios).
 */
public enum TipoTecnologiaEnum {
    /**
     * Lenguaje de programación (ej. Java, TypeScript, Python).
     */
    LENGUAJE,

    /**
     * Framework o biblioteca de desarrollo (ej. Spring Boot, Angular, React).
     */
    FRAMEWORK,

    /**
     * Motor o sistema de gestión de bases de datos (ej. PostgreSQL, MongoDB).
     */
    BASE_DE_DATOS,

    /**
     * Herramientas o tecnologías asociadas a DevOps y despliegue (ej. Docker, Kubernetes, Jenkins).
     */
    DEVOPS,

    /**
     * Herramienta de software general de apoyo (ej. Git, IntelliJ, Postman).
     */
    HERRAMIENTA,

    /**
     * Otra tecnología no clasificada en las categorías anteriores.
     */
    OTHER
}