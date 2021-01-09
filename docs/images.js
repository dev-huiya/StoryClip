const images = [
    {
        url: "/images/{hash}",
        method: "ALL",
        urls: [
            {
                url: "/{hash}",
                method: "ALL"
            },
            {
                url: "/images/{hash}",
                method: "ALL"
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
]