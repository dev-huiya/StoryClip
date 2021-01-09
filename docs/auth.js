const auth = [
    {
        url: "/auth/signin",
        method: "POST",
        title: "로그인",
        description: `입력받은 사용자 정보로 로그인하고 JWT 토큰을 발급받습니다.`,
        params: [
            {
                name: "email",
                required: true,
                type: "String",
                description: "이메일 (아이디)"
            },
            {
                name: "password",
                required: true,
                type: "String",
                description: "비밀번호"
            },
            {
                name: "recaptchaToken",
                required: true,
                type: "String",
                description: "구글 리캡챠 토큰"
            },
            {
                name: "autoLogin",
                required: false,
                type: "Boolean",
                description: "자동로그인 여부",
                default: "false"
                // TODO: 추후에는 기본값을 UI에 표시해야함
            },
        ],
        request: `query({
    url: "/account/signin",
    method: "POST",
    data: {
        "email": "test@test.com",
        "password": "1234",
        "recaptchaToken": String
    },
})
.then((res) => {
    console.log(res)
}`,
        response: {
            success: `HTTP/1.1 200 OK

{
	"success": true,
	"message": "OK",
	"resultData": {
		"usage": true | false
	}
}`,
            fail: [
                `HTTP/1.1 200 OK

{
    "success": false,
    "message": "AUTH_WRONG" | "JWT_ERROR",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "usage",
                    always: true,
                    type: "Boolean",
                    description: "성공여부"
                }
            ],
        }
    },
    {
        url: "/auth/signout",
        method: "DELETE",
        title: "로그아웃",
        description: "로그아웃하고 토큰을 폐기합니다.",
        headers: [
            {
                name: "Authorization",
                required: true,
                description: "JWT Token",
            },
        ],
        request: `query({
    url: "/auth/signout",
    method: "GET",
})
.then((res) => {
    console.log(res);
})`,
        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "signout": true
    }
}`,
            fail: [
                `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "signout",
                    always: true,
                    type: "Boolean",
                    description: "폐기 성공 여부"
                },
            ],
        }
    },
    {
        url: "/auth/refresh",
        method: "PUT",
        title: "JWT 토큰 갱신",
        description: "refresh_token으로 access_token을 재발급 받습니다.",
        params: [
            {
                name: "refreshToken",
                required: true,
                type: "String",
                description: "JWT 발급시 전달 받은 refresh_token"
            },
        ],
        request: `query({
    url: "/auth/refresh",
    method: "PUT",
    data: {
        refreshToken: String
    }
})
.then((res) => {
    console.log(res);
})`,
        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "token": "eyJ0eXAiOiJKV1QiLCJhbG...",
        "browser": "Windows 10 / Chrome 87",
        "publicKey": "-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQ...pDSQIDAQAB\\n-----END PUBLIC KEY-----\\n"
    }
}`,
            fail: [
                `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "token",
                    always: true,
                    type: "String",
                    description: "엑세스 토큰"
                },
                {
                    name: "browser",
                    always: true,
                    type: "String",
                    description: "접속한 브라우저 정보"
                },
                {
                    name: "publicKey",
                    always: true,
                    type: "String",
                    description: "토큰 검증에 사용하는 공개키"
                },
            ],
        }
    },
    {
        url: "/auth/verify",
        method: "GET",
        title: "JWT 토큰 검증",
        description: "access_token이 사용 가능한지 검증합니다.",
        headers: [
            {
                name: "Authorization",
                required: false,
                description: "JWT Token",
            },
        ],
        request: `query({
    url: "/auth/verify",
    method: "GET",
})
.then((res) => {
    console.log(res);
})`,
        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "verify": true | false
    }
}`,
            fail: [
                `HTTP/1.1 500 Internal Server Error

{
    "success": false,
    "message": "SERVER_ERROR",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "verify",
                    always: true,
                    type: "Boolean",
                    description: "검증 성공 여부"
                },
            ],
        }
    },
    {
        url: "/auth/key",
        method: "GET",
        title: "토큰 공개키 요청",
        description: "JWT를 검증하는데 사용할 공개키를 요청합니다.",
        headers: [
            {
                name: "Authorization",
                required: true,
                description: "JWT Token",
            },
        ],
        request: `query({
    url: "/auth/key",
    method: "GET",
})
.then((res) => {
    console.log(res);
})`,
        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "publicKey": "-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQEFA...jWTpMvw+bNwIDAQAB\\n-----END PUBLIC KEY-----\\n"
    }
}`,
            fail: [
                `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "publicKey",
                    always: true,
                    type: "String",
                    description: "토큰 검증에 사용하는 공개키"
                },
            ],
        }
    },
]