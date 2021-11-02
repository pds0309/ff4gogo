package com.pds.serverlessapi.api;

import com.pds.serverlessapi.config.SlApiConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;



import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SlApiConfig.class,HttpEntity.class})
class ConSlApiTest {



    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private HttpHeaders headers;

    @Test
    void getKeyTest() {
        assertNotNull(headers);
        assertNotNull(headers.get("x-api-key"));
    }


    @Test
    void conApiButNoBodyTest(){
        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class,()->
        restTemplate.exchange("https://0o2ma3bzal.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, new HttpEntity<>("",headers),String.class ));
        assertEquals(504, exception.getRawStatusCode());
    }
    @Test
    void conApiExceptionInvalidKeyTest(){
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class,()->
                restTemplate.exchange("https://0o2ma3bzal.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, new HttpEntity("",new HttpHeaders()),String.class ));
        assertEquals(403 , exception.getRawStatusCode());
    }

    private final String exampleBody = "{\"assist\": 0, \"asx\": 0 , \"asy\": 0, \"fin\": 1, \"hed\": 0, \"inv\": 0, \"nom\": 0\n" +
            ", \"x\": 0.88 ,\"y\": 0.45}";

    @Test
    void conApiWithBodyTest(){
        HttpEntity<String> httpEntityHasBody = new HttpEntity<>(exampleBody, headers);
        assertEquals(headers,httpEntityHasBody.getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://0o2ma3bzal.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, httpEntityHasBody, String.class);
        assertEquals(200 , responseEntity.getStatusCodeValue());
        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertTrue(jsonObject.has("prediction"));
    }

    @Test
    void conLogresListApiTest(){
        String request = "{\n" +
                "    \"inner\":\n" +
                "    [\n" +
                "    {\"assist\": 0, \"asx\": 0 , \"asy\": 0, \"fin\": \"1\", \"hed\": 0, \"inv\": 0, \"nom\": 0\n" +
                ", \"x\": 0.88 ,\"y\": 0.45},\n" +
                "{\"assist\": 0, \"asx\": 0 , \"asy\": 0, \"fin\": \"1\", \"hed\": 0, \"inv\": 0, \"nom\": 0\n" +
                ", \"x\": 0.88 ,\"y\": 0.45 }\n" +
                "    ]\n" +
                "}";
        HttpEntity<String> httpEntityHasBody = new HttpEntity<>(request, headers);
        assertEquals(headers,httpEntityHasBody.getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://qgp7hgv778.execute-api.ap-northeast-2.amazonaws.com/Prod/xgrf", HttpMethod.POST, httpEntityHasBody, String.class);
        assertEquals(200 , responseEntity.getStatusCodeValue());
        JSONArray resultArray = new JSONArray(responseEntity.getBody());
        assertEquals(2,resultArray.length());
        assertTrue(resultArray.getJSONObject(0).has("prediction"));
    }

}
