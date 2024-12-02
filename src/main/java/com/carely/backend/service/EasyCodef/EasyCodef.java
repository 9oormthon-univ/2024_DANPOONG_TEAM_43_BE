package com.carely.backend.service.EasyCodef;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.carely.backend.code.EasyCodefMessageConstant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class EasyCodef {


    private ObjectMapper mapper = new ObjectMapper();

    /**
     * EasyCodef 사용을 위한 변수 설정 오브젝트
     */
    @Autowired
    private EasyCodefProperties properties = new EasyCodefProperties();

    public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
        properties.setClientInfoForDemo(demoClientId, demoClientSecret);
    }

    public void setPublicKey(String publicKey) {
        properties.setPublicKey(publicKey);
    }

    public String getPublicKey() {
        return properties.getPublicKey();
    }


    public EasyCodefResponse requestProduct(String productUrl, int serviceType, HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String clientId = properties.getDemoClientId();
        String clientSecret = properties.getDemoClientSecret();

        if (clientId == null || clientSecret == null) {
            throw new IllegalArgumentException("Client ID or Secret is not set");
        }

        boolean validationFlag = true;

        /**	#1.필수 항목 체크 - 클라이언트 정보	*/
        validationFlag = checkClientInfo(1);
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.EMPTY_CLIENT_INFO);
            return response;
        }

        /**	#2.필수 항목 체크 - 퍼블릭 키	*/
        validationFlag = checkPublicKey();
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.EMPTY_PUBLIC_KEY);
            return response;
        }

        /**	#3.추가인증 키워드 체크	*/
        validationFlag = checkTwoWayKeyword(parameterMap);
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.INVALID_2WAY_KEYWORD);
            return response;
        }

        /**	#4.상품 조회 요청	*/
        EasyCodefResponse response = EasyCodefConnector.execute(productUrl, 1, parameterMap, properties);

        /**	#5.결과 반환	*/
        return response;
    }

    public EasyCodefResponse requestCertification(String productUrl, int serviceType, HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        String clientId = properties.getDemoClientId();
        String clientSecret = properties.getDemoClientSecret();

        if (clientId == null || clientSecret == null) {
            throw new IllegalArgumentException("Client ID or Secret is not set");
        }

        boolean validationFlag = true;

        /**	#1.필수 항목 체크 - 클라이언트 정보	*/
        validationFlag = checkClientInfo(1);
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.EMPTY_CLIENT_INFO);
            return response;
        }

        /**	#2.필수 항목 체크 - 퍼블릭 키	*/
        validationFlag = checkPublicKey();
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.EMPTY_PUBLIC_KEY);
            return response;
        }

        /**	#3.추가인증 파라미터 필수 입력 체크	*/
        validationFlag = checkTwoWayInfo(parameterMap);
        if(!validationFlag) {
            EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.INVALID_2WAY_INFO);
            return response;
        }

        /**	#4.상품 조회 요청	*/
        EasyCodefResponse response = EasyCodefConnector.execute(productUrl, 1, parameterMap, properties);

        /**	#5.결과 반환	*/
        return response;
    }


    /**
     * Desc : 서비스 타입에 따른 클라이언트 정보 설정 확인
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:33:23 PM
     * @param serviceType
     * @return
     */
    private boolean checkClientInfo(int serviceType) {
        if(properties.getDemoClientId() == null || "".equals(properties.getDemoClientId().trim())) {
            return false;
        }
        if(properties.getDemoClientSecret() == null || "".equals(properties.getDemoClientSecret().trim())) {
            return false;
        }
        return true;
    }

    /**
     * Desc : 퍼블릭키 정보 설정 확인
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:33:31 PM
     * @return
     */
    private boolean checkPublicKey() {
        if(properties.getPublicKey() == null || "".equals(properties.getPublicKey().trim())) {
            return false;
        }
        return true;
    }

    /**
     * Desc : 추가인증 파라미터 설정 확인
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:33:39 PM
     * @param parameterMap
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean checkTwoWayInfo(HashMap<String, Object> parameterMap) {
        // is2Way 확인
        if (!(parameterMap.getOrDefault("is2Way", false) instanceof Boolean) || !(boolean) parameterMap.get("is2Way")) {
            return false;
        }

        // twoWayInfo 확인
        Object twoWayInfo = parameterMap.get("twoWayInfo");
        if (!(twoWayInfo instanceof HashMap)) {
            return false;
        }

        HashMap<String, Object> twoWayInfoMap = (HashMap<String, Object>) twoWayInfo;

        // 필수 필드 확인
        String[] requiredFields = {"jobIndex", "threadIndex", "jti", "twoWayTimestamp"};
        for (String field : requiredFields) {
            if (!twoWayInfoMap.containsKey(field)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Desc : 추가인증 키워드 확인
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:33:45 PM
     * @param parameterMap
     * @return
     */
    private boolean checkTwoWayKeyword(HashMap<String, Object> parameterMap) {
        if(parameterMap != null && (parameterMap.containsKey("is2Way") || parameterMap.containsKey("twoWayInfo"))) {
            return false;
        }

        return true;
    }



    public EasyCodefResponse createAccount(HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.CREATE_ACCOUNT, 1, parameterMap);
    }

    public EasyCodefResponse addAccount(HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.ADD_ACCOUNT, 1, parameterMap);
    }

    /**
     * Desc : 계정 정보 수정
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:34:21 PM
     * @param serviceType
     * @param parameterMap
     * @return
     * @throws UnsupportedEncodingException
     * @throws JsonProcessingException
     * @throws InterruptedException
     */
    public EasyCodefResponse updateAccount(int serviceType, HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.UPDATE_ACCOUNT, serviceType, parameterMap);
    }

    /**
     * Desc : 계정 정보 삭제
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Jun 26, 2020 3:34:30 PM
     * @param serviceType
     * @param parameterMap
     * @return
     * @throws UnsupportedEncodingException
     * @throws JsonProcessingException
     * @throws InterruptedException
     */
    public EasyCodefResponse deleteAccount(int serviceType, HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.DELETE_ACCOUNT, serviceType, parameterMap);
    }

    public EasyCodefResponse getAccountList(int serviceType, HashMap<String, Object> parameterMap) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.GET_ACCOUNT_LIST, serviceType, parameterMap);
    }

    public EasyCodefResponse getConnectedIdList(int serviceType) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
        return requestProduct(EasyCodefConstant.GET_CID_LIST, serviceType, null);
    }

    public String requestToken(int serviceType) throws JsonParseException, JsonMappingException, IOException {
        String clientId = properties.getDemoClientId();
        String clientSecret = properties.getDemoClientSecret();
        String accessToken = EasyCodefTokenMap.getToken(clientId); // 보유 중인 토큰이 있는 경우 반환
        if(accessToken != null) {
            HashMap<String, Object> tokenMap = EasyCodefUtil.getTokenMap(accessToken);
            if(EasyCodefUtil.checkValidity((int)(tokenMap.get("exp")))) {	// 토큰의 유효 기간 확인
                return accessToken;	// 정상 토큰인 경우 반환
            }
        }

        HashMap<String, Object> tokenMap = EasyCodefConnector.publishToken(clientId, clientSecret);	// 보유 중인 토큰이 없거나 신규 발급 조건에 해당하는 경우 발급 후 반환(만료일시를 지났거나 한시간 이내로 도래한 경우 신규 발급)
        if(tokenMap != null) {
            accessToken = (String)tokenMap.get("access_token");
            EasyCodefTokenMap.setToken(clientId, accessToken);	// 발급 토큰 저장
            return accessToken;
        } else {
            return null;
        }
    }

    /**
     * Desc : 토큰 신규 발급 후 반환(코드에프 이용 중 추가 업무 사용을 하는 등 토큰 권한 변경이 필요하거나 신규 토큰이 필요한 경우시 사용)
     * @Company : ©CODEF corp.
     * @Author  : notfound404@codef.io
     * @Date    : Sep 16, 2020 11:58:32 AM
     * @param serviceType
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public String requestNewToken(int serviceType) throws JsonParseException, JsonMappingException, IOException {
        String clientId = properties.getDemoClientId();
        String clientSecret = properties.getDemoClientSecret();

        String accessToken = null;
        HashMap<String, Object> tokenMap = EasyCodefConnector.publishToken(clientId, clientSecret);	// 토큰 신규 발급
        if(tokenMap != null) {
            accessToken = (String)tokenMap.get("access_token");
            EasyCodefTokenMap.setToken(clientId, accessToken);	// 발급 토큰 저장
            return accessToken;
        } else {
            return null;
        }
    }


    public String processJson(String jsonString) throws IOException {
        // JSON 문자열을 ObjectMapper로 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);

        // data 객체 접근
        JsonNode dataNode = rootNode.get("data");

        // continue2Way 값 확인
        boolean continue2Way = dataNode.get("continue2Way").asBoolean();

        // 조건 처리
        if (continue2Way) {
            return "Additional authentication required.";
        } else {
            return "Request processed successfully.";
        }
    }

}