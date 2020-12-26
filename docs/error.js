const errorCodes = [
    {
        name: "AUTH_REQURED",
        type: "Auth",
        description: "로그인 필요"
    },
    {
        name: "AUTH_WRONG",
        type: "Auth",
        description: "잘못된 계정 정보 (아이디 혹은 비밀번호)"
    },
    {
        name: "JWT_EXPIRED_ERROR",
        type: "Auth",
        description: "JWT 만료됨"
    },
    {
        name: "JWT_VERIFY_ERROR",
        type: "Auth",
        description: "JWT 검증 실패"
    },
    {
        name: "JWT_INVALID_CLAIM",
        type: "Auth",
        description: "올바르지 않은 토큰 정보"
    },
    {
        name: "JWT_ALGORITHM_ERROR",
        type: "Auth",
        description: "지원하지 않는 알고리즘 : 서버 에러"
    },
    {
        name: "JWT_ERROR",
        type: "Auth",
        description: "JWT 생성 실패 : 서버 에러"
    },
    {
        name: "JWT_KEY_EMPTY",
        type: "Auth",
        description: "JWT 암호화 키가 없음 : 서버 에러"
    },
    {
        name: "JOIN_DUPLICATE",
        type: "Auth",
        description: "회원 가입시 계정정보 중복됨"
    },
    {
        name: "CAPTCHA_EMPTY",
        type: "Auth",
        description: "캡챠 토큰 없음"
    },
    {
        name: "CAPTCHA_FAIL",
        type: "Auth",
        description: "캡챠 인증 실패"
    },
    {
        name: "PARAM_REQUIRED",
        type: "Http",
        description: "필수값 누락됨"
    },
    {
        name: "FORBIDDEN",
        type: "Http",
        description: "403 Forbidden"
    },
    {
        name: "NOT_FOUND",
        type: "Http",
        description: "404 Not found"
    },
    {
        name: "METHOD_NOT_ALLOWED",
        type: "Http",
        description: "405 Method Not Allowed"
    },
    {
        name: "SERVER_ERROR",
        type: "Http",
        description: "500 Server error"
    },
    {
        name: "RESULT_NOT_SET",
        type: "Type",
        description: "결과가 등록되지 않음"
    },
]