const apis = {
    "사용자": [
        {
            url: "/account/signup-check/email",
            method: "GET",
            title: "이메일 중복 확인",
            description: "회원가입 전 사용자가 입력한 이메일을 중복 확인 확인합니다.",
            params: [
                {
                    name: "email",
                    required: true,
                    type: "String",
                    description: "사용자가 입력한 이메일"
                },
            ],
            response: {
                success: `{
                    "success": true,
                    "message": "OK",
                    "resultData": {
                        "usage": true | false
                    }
                }`,
                fail: [``],
                description: ""
            }
        },
        {
            url: "/account/signup",
            method: "POST",
            title: "회원가입",
            description: "회원가입",
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
            response: {
                success: `{
                    "success": true,
                    "message": "OK",
                    "resultData": {
                        "join": true
                    }
                }`,
                fail: [``],
                description: ""
            }
        },
    ],
    "기타": [
        {
            url: "/images/{hash}",
            method: "GET",
            title: "이미지",
            description: "업로드된 이미지를 불러오는 API 입니다",
            header: [
                {
                    name: "Authorization",
                    required: true,
                    type: "String",
                    description: "JWT Token"
                },
            ],
            response: {
                success: `이미지 byte[]`,
                fail: [
                    `{
                        "success": false,
                        "message": "AUTH_REQURED",
                        "resultData": null
                    }`,
                    `{
                        "success": false,
                        "message": "JWT_EXPIRED_ERROR",
                        "resultData": null
                    }`
                ],
                description: ""
            }
        },
        {
            url: "/images/{hash}",
            method: "PUT",
            title: "이미지",
            description: "업로드된 이미지를 불러오는 ",
            header: [
                {
                    name: "Authorization",
                    required: true,
                    type: "String",
                    description: "JWT Token"
                },
            ],
            response: {
                success: `이미지 byte[]`,
                fail: [
                    `{
                        "success": false,
                        "message": "AUTH_REQURED",
                        "resultData": null
                    }`,
                    `{
                        "success": false,
                        "message": "JWT_EXPIRED_ERROR",
                        "resultData": null
                    }`
                ],
                description: ""
            }
        },
        {
            url: "/images/{hash}",
            method: "DELETE",
            title: "이미지",
            description: "업로드된 이미지를 불러오는 ",
            header: [
                {
                    name: "Authorization",
                    required: true,
                    type: "String",
                    description: "JWT Token"
                },
            ],
            response: {
                success: `이미지 byte[]`,
                fail: [
                    `{
                        "success": false,
                        "message": "AUTH_REQURED",
                        "resultData": null
                    }`,
                    `{
                        "success": false,
                        "message": "JWT_EXPIRED_ERROR",
                        "resultData": null
                    }`
                ],
                description: ""
            }
        },
    ],
};