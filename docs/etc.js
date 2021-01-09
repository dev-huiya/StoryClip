const etc = [
    {
        url: "/",
        method: "ALL",
        urls: [
            {
                url: "/",
                method: "ALL"
            },
            {
                url: "/status",
                method: "ALL"
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
]