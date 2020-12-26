package io.storyclip.web.Type;

public enum Http {
    /**
     * 200 OK
     */
    OK,

    /**
     * 403 Forbidden
     */
    FORBIDDEN,

    /**
     * 404 Not found
     */
    NOT_FOUND,

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED,

    /**
     * 500 Server error
     */
    SERVER_ERROR,

    /**
     * 필수값 누락됨
     */
    PARAM_REQUIRED,
}
