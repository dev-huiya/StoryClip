const user = [
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
        url: "/account/info",
        method: "GET",
        title: "회원 정보 조회",
        description: "회원정보를 조회합니다.",
        request: `query({
    url: '/account/info',
    method: 'GET'
}).then(res=>{
    console.log(res)
})`,

        response: {
            success: `HTTP/1.1 200 OK

{
   "success": true,
    "message": "OK",
    "resultData": {
        "email": "test@test.com",
        "createDate": "2020-12-22 22:35:47.097",
        "lastDate": "2021-01-09 01:43:25.000",
        "penName": "테스",
        "profile": "59a82d9ed6a21d5731b5a94db32a990deced4a709d3d26b35fa4ec1f34b0bdcb",
        "kakaoAccountId": null
    }
}`,
            fail: [ ``, ],
            params: [
                {
                    name: "email",
                    always: true,
                    type: "String",
                    description: "이메일"
                },
                {
                    name: "createDate",
                    always: true,
                    type: "String",
                    description: "최초 생성 날짜"
                },
                {
                    name: "lastDate",
                    always: true,
                    type: "String",
                    description: "마지막 접속 날짜"
                },
                {
                    name: "penName",
                    always: true,
                    type: "String",
                    description: "필명"
                },
                {
                    name: "profile",
                    always: true,
                    type: "File",
                    description: "프로필 사진"
                },
                {
                    name: "kakaoAccountId",
                    always: true,
                    type: "file",
                    description: "카카오 아이디"
                }
            ],
        }
    },
    {
        url: "/account/info",
        method: "PATCH",
        title: "회원 정보 수정",
        description: "회원정보를 수정합니다.<br />요청 파라매터 중 하나는 필수적으로 보내야합니다.",
        request: `const formData = new FormData();
formData.append('penName', String);
formData.append('profile', File);
		
query({
    url: "/account/info",
    method: "PATCH",
    data: formData
})
.then((res) => {
    console.log(res);
})`,
        info: [
            {
                type: 'error',
                message : '프로필 이미지 업로드를 위해 multipart/form-data 형식으로 전달되어야 합니다.'
            },
            {
                type: 'info',
                message : '사용자 정보 변경 후에는 GET /account/info를 통해서 사용자 정보를 다시 불러와야 합니다.'
            }
        ],
        headers: [
            {
                name: "Authorization",
                required: true,
                description: "JWT Token",
            },
        ],
        params: [
            {
                name: "penName",
                required: false,
                type: "String",
                description: "필명"
            },
            {
                name: "profile",
                required: false,
                type: "File",
                description: "사용자 프로필 이미지"
            },
        ],
        response: {
            success:`HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "update": true
    }
}`,
            fail: [ `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`, ],
            params: [
                {
                    name: "update",
                    always: true,
                    type: "Boolean",
                    description: "회원정보 수정 성공 여부"
                }
            ],
        }
    },
    {
        url: "/account/password",
        method: "PATCH",
        title: "비밀번호 수정",
        description: "비밀번호를 수정합니다.",
        request: `query({
    url: '/account/password',
    method: 'PATCH',
    data: {
        "password": "1234",
        "newPassword": "test"
    },
}).then(res=>{
    console.log(res)
})`,
        params: [
            {
                name: "Authorization",
                required: true,
                type: "",
                description: "JWT Token"
            },
            {
                name: "password",
                always: true,
                type: "String",
                description: "현재 사용 비밀번호"
            },
            {
                name: "newPassword",
                always: true,
                type: "String",
                description: "변경할 비밀번호"
            }
        ],

        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": {
        "update": true
    }
}`,
            fail: [ `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
                `HTTP/1.1 200 OK

{
    "success": false,
    "message": "PARAM_REQUIRED" | "PASSWORD_CHANGE_FAIL",
    "resultData": null
}`],
            params: [
                {
                    name: "update",
                    always: true,
                    type: "Boolean",
                    description: "비밀번호 변경 성공 여부"
                }
            ],
        }
    }
]