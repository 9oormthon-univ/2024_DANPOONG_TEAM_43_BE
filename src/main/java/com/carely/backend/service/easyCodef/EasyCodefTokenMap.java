package com.carely.backend.service.easyCodef;


import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EasyCodefTokenMap {

    /**	쉬운 코드에프 이용을 위한 토큰 저장 맵	*/
    private static HashMap<String, String> ACCESS_TOKEN_MAP = new HashMap<String, String>();

    /**
     * Desc : 토큰 저장
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:41:21 PM
     */
    public static void setToken(String clientId, String accessToken) {
        ACCESS_TOKEN_MAP.put(clientId, accessToken);
    }

    /**
     * Desc : 토큰 반환
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:41:28 PM
     */
    public static String getToken(String clientId) {
        return ACCESS_TOKEN_MAP.get(clientId);
    }
}