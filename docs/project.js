const project = [
    {
        url: "/project/new",
        method: "POST",
        title: "작품 생성",
        description: `신규 작품을 생성합니다.`,
        params: [
            {
                name: "title",
                required: true,
                type: "String",
                description: "작품 제목"
            },
            {
                name: "description",
                required: true,
                type: "String",
                description: "작품 설명"
            },
        ],
        request: `query({
    url: "/project/new",
    method: "POST",
    data: {
        "title": "작품 제목2",
        "description": "설명2"
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
		"projectId": "aUpfiUgeSNbOujYo",
        "title": "작품 제목",
        "description": "설명",
        "createDate": null,
        "modifyDate": null
	}
}`,
            fail: [
                `HTTP/1.1 200 OK

{
    "success": false,
    "message": "AUTH_WRONG" | "JWT_ERROR",
    "resultData": null
}`,
`HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
            ],
            params: [
                {
                    name: "projectId",
                    always: true,
                    type: "String",
                    description: "작품 ID"
                },
                {
                    name: "title",
                    always: true,
                    type: "String",
                    description: "작품 제목"
                },
                {
                    name: "description",
                    always: true,
                    type: "String",
                    description: "작품 설명"
                },
                {
                    name: "createDate",
                    always: true,
                    type: "String",
                    description: "작품 생성일"
                },
                {
                    name: "modifyDate",
                    always: true,
                    type: "String",
                    description: "작품 수정일"
                },
            ],
        }
    },
    {
        url: "/project/list",
        method: "GET",
        urls: [
            {
                url: "/project/list",
                method: "GET"
            },
            {
                url: "/projects",
                method: "GET"
            }
        ],
        title: "작품 목록",
        description: "작품 목록을 불러오는 API 입니다",
        headers: [
            {
                name: "Authorization",
                required: true,
                description: "JWT Token",
            },
        ],
        request: `query({
    url: "/project/list",
    method: "GET",
})
.then((res) => {
    console.log(res)
}`,
        response: {
            success: `HTTP/1.1 200 OK

{
    "success": true,
    "message": "OK",
    "resultData": [
        {
            "projectId": "aUpfiUgeSNbOujYo",
            "title": "작품 제목",
            "description": "설명",
            "createDate": "2021-01-09 23:57:06.837",
            "modifyDate": "2021-01-09 23:57:06.837"
        },
        {
            "projectId": "vhwJmtMFrcSAiipW",
            "title": "작품 제목2",
            "description": "설명2",
            "createDate": "2021-01-09 23:57:23.881",
            "modifyDate": "2021-01-09 23:57:23.881"
        }
    ]
}`,
            params: [
                {
                    name: "",
                    always: true,
                    type: "Array",
                    description: "작품 목록 배열"
                },
                {
                    name: "projectId",
                    always: true,
                    type: "String",
                    description: "└ 작품 ID"
                },
                {
                    name: "title",
                    always: true,
                    type: "String",
                    description: "└ 작품 제목"
                },
                {
                    name: "description",
                    always: true,
                    type: "String",
                    description: "└ 작품 설명"
                },
                {
                    name: "createDate",
                    always: true,
                    type: "String",
                    description: "└ 작품 생성일"
                },
                {
                    name: "modifyDate",
                    always: true,
                    type: "String",
                    description: "└ 작품 수정일"
                },
            ],
            fail: [
                `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
            ],
        }
    },
    {
        url: "/PJ{projectId}",
        method: "GET",
        title: "작품 정보",
        description: "작품 정보를 불러오는 API 입니다",
        headers: [
            {
                name: "Authorization",
                required: true,
                description: "JWT Token",
            },
        ],
        params: [
            {
                name: "projectId",
                required: true,
                type: "PathVariable String",
                description: "프로젝트 ID"
            },
        ],
        request: `query({
    url: "/PJ" + projectId,
    method: "GET",
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
        "projectId": "aUpfiUgeSNbOujYo",
        "title": "작품 제목",
        "description": "설명",
        "createDate": "2021-01-09 23:57:06.837",
        "modifyDate": "2021-01-09 23:57:06.837"
    }
}`,
            params: [
                {
                    name: "projectId",
                    always: true,
                    type: "String",
                    description: "작품 ID"
                },
                {
                    name: "title",
                    always: true,
                    type: "String",
                    description: "작품 제목"
                },
                {
                    name: "description",
                    always: true,
                    type: "String",
                    description: "작품 설명"
                },
                {
                    name: "createDate",
                    always: true,
                    type: "String",
                    description: "작품 생성일"
                },
                {
                    name: "modifyDate",
                    always: true,
                    type: "String",
                    description: "작품 수정일"
                },
            ],
            fail: [
                `HTTP/1.1 401 Unauthorized

{
    "success": false,
    "message": "AUTH_REQURED" | "JWT_EXPIRED_ERROR",
    "resultData": null
}`,
            ],
        }
    },
]