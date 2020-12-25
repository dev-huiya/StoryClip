const apis = {
    "사용자": [
        {
            url: "/account/signup-check/email",
            method: "GET",
            title: "이메일 중복 확인",
            description: "회원가입 전 사용자가 입력한 이메일을 중복 확인합니다.",
            params: [
                {
                    name: "email",
                    required: true,
                    type: "String",
                    description: "사용자가 입력한 이메일"
                },
            ],
            request: `query({
    url: '/account/signup-check/email',
    method: 'GET',
    data: {
        email: 'test@test.com'
    }
}).then(res=>{
    console.log(res)
})`,
            response: {
                success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "usage": true | false
    }
}`,
                fail: [``],
                params: [
                    {
                        name: "usage",
                        always: true,
                        type: "Boolean",
                        description: "사용 가능 여부"
                    },
                ],
            }
        },
        {
            url: "/account/signup",
            method: "POST",
            title: "회원가입",
            description: `입력받은 사용자 정보로 회원가입을 합니다.`,
            info: [
                {
                    type: 'error',
                    message : '프로필 이미지 업로드를 위해 multipart/form-data 형식으로 전달되어야 합니다.'
                }
            ],
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
                    name: "penName",
                    required: true,
                    type: "String",
                    description: "필명"
                },
                {
                    name: "recaptchaToken",
                    required: true,
                    type: "String",
                    description: "구글 리캡챠 토큰"
                },
                {
                    name: "profile",
                    required: false,
                    type: "File",
                    description: "사용자 프로필 이미지"
                },
            ],
            request: `const formData = new FormData();
formData.append('email', String);
formData.append('password', String);
formData.append('penName', String);
formData.append('profile', File);
formData.append('recaptchaToken', String);

query({
    url: "/account/signup",
    method: "POST",
    data: formData,
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
        "join": true
    }
}`,
                fail: [
`HTTP/1.1 200 OK

{
    "success": false,
    "message": "AUTH_WRONG" | "JOIN_DUPLICATE" | "CAPTCHA_EMPTY" | "CAPTCHA_FAIL",
    "resultData": null
}`,
                ],
                params: [
                    {
                        name: "join",
                        always: true,
                        type: "Boolean",
                        description: "가입 성공 여부"
                    },
                ],
            }
        },
        {
            url: "/account/signin",
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
            ],
            request: `query({
    url: "/account/signin",
    method: "POST",
    data: {
        "email": "test@test.com",
        "password": "1234"
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
        "token": "eyJ0eXAiOiJKV1QiLCJhbG...",
        "browser": "Windows 10 / Chrome 87",
        "publicKey": "-----BEGIN PUBLIC KEY-----\\nMIIBIjANBgkqhkiG9w0BAQ...pDSQIDAQAB\\n-----END PUBLIC KEY-----\\n"
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
    ],
    "인증": [
        {
            url: "/auth/refresh",
            method: "PUT",
            title: "JWT 토큰 갱신",
            description: "refresh_token으로 access_token을 재발급 받습니다.",
            params: [
                {
                    name: "refresh",
                    required: true,
                    type: "String",
                    description: "JWT 발급시 전달 받은 refresh_token"
                },
            ],
            request: `query({
    url: "/auth/refresh",
    method: "PUT",
    data: {
        requestToken: String
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
                    `HTTP/1.1 200 OK

{
    "success": false,
    "message": "AUTH_WRONG" 
                | "JWT_ERROR",
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
    ],
    "이미지": [
        {
            url: "/images/{hash}",
            method: "GET",
            urls: [
                {
                    url: "/{hash}",
                    method: "GET"
                },
                {
                    url: "/images/{hash}",
                    method: "GET"
                }
            ],
            title: "이미지",
            description: "업로드된 이미지를 불러오는 API 입니다",
            headers: [
                {
                    name: "Authorization",
                    required: true,
                    description: "JWT Token",
                },
            ],
            params: [
                {
                    name: "hash",
                    required: true,
                    type: "PathVariable String",
                    description: "이미지 파일 해시값"
                },
            ],
            request: ``,
            response: {
                success: `HTTP/1.1 200 OK

이미지 byte[]`,
                fail: [
                    `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
`HTTP/1.1 403 Forbidden

{
    "success": false,
    "message": "FORBIDDEN",
    "resultData": null
}`,
                ],

            }
        },
    ],
    "기타": [
        {
            url: "/",
            method: "GET",
            urls: [
                {
                    url: "/",
                    method: "GET"
                },
                {
                    url: "/status",
                    method: "GET"
                }
            ],
            title: "서버 정보",
            description: "기본적인 서버 정보를 요청합니다.",
            request: `query({
    url: "/",
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
        "isServerRun": true,
        "version": "0.1.0-SNAPSHOT"
    }
}`,
                fail: [``],
                params: [
                    {
                        name: "isServerRun",
                        always: true,
                        type: "Boolean",
                        description: "서버 가동 여부"
                    },
                    {
                        name: "version",
                        always: true,
                        type: "String",
                        description: "서버 버전 정보"
                    },
                ],
            }
        },
    ],
};