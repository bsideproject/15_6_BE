package bside.NotToDoClub.domain_name.auth.config.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppleOauth {

    /**
     *     apple:
     *       url:
     *       team-id:
     *       redirect-url:
     *       client-id:
     *       key-id:
     *       key-path:
     */
    @Value("${spring.OAuth2.apple.url}")
    private String APPLE_LOGIN_URL;

    @Value("${spring.OAuth2.apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${spring.OAuth2.apple.redirect-url}")
    private String APPLE_REDIRECT_URL;

    @Value("${spring.OAuth2.apple.client-id}")
    private String APPLE_CLIENT_ID;

    @Value("${spring.OAuth2.apple.key-id}")
    private String APPLE_KEY_ID;

    @Value("${spring.OAuth2.apple.key-path}")
    private String APPLE_KEY_PATH;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public String getOauthRedirectURL(){
        // APPLE_LOGIN_URL + "/auth/authorize?client_id=" + client_id
        // + "&redirect_uri=" + redirect_uri
        // + "&response_type=code id_token&response_mode=form_post
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", APPLE_CLIENT_ID);
        params.put("redirect_uri", APPLE_REDIRECT_URL);
        params.put("response_type", "code id_token");
        params.put("scope", "email name");
        params.put("response_mode", "form_post");

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = APPLE_LOGIN_URL + "/auth/authorize?" + parameterString;
        log.info("redirectURL = {}", redirectURL);

        return redirectURL;
    }

    public String getApiResponse(String code){
        String client_id = APPLE_CLIENT_ID;
        String client_secret = createClientSecret(APPLE_TEAM_ID, APPLE_CLIENT_ID,
                APPLE_KEY_ID, APPLE_KEY_PATH, APPLE_LOGIN_URL);

        String reqUrl = APPLE_LOGIN_URL + "/auth/token";

        Map<String, String> tokenRequest = new HashMap<>();

        tokenRequest.put("client_id", client_id);
        tokenRequest.put("client_secret", client_secret);
        tokenRequest.put("code", code);
//        tokenRequest.put("scope", "name email");
        tokenRequest.put("grant_type", "authorization_code");

        log.info("api response param = {}", tokenRequest);

        String apiResponse = doPost(reqUrl, tokenRequest);
        log.info("api response = {}", apiResponse);
        return apiResponse;
    }

    public String createClientSecret(String teamId, String clientId, String keyId, String keyPath, String authUrl) {

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        Date now = new Date();

        claimsSet.setIssuer(teamId);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
        claimsSet.setAudience(authUrl);
        claimsSet.setSubject(clientId);

        SignedJWT jwt = new SignedJWT(header, claimsSet);

        try {
//            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey(keyPath));
//            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readPrivateKey(keyPath));
            KeyFactory kf = KeyFactory.getInstance("EC");
            ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());

            jwt.sign(jwsSigner);

        } catch (JOSEException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return jwt.serialize();
    }


    private byte[] readPrivateKey(String keyPath) {

        ClassPathResource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try (Reader keyReader = new InputStreamReader(resource.getInputStream());
             PemReader pemReader = new PemReader(keyReader)) {
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public String doPost(String url, Map<String, String> param) {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        Integer statusCode = null;
        String reasonPhrase = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Map.Entry<String, String>> entrySet = param.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();
                nvps.add(new BasicNameValuePair(fieldName, fieldValue));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            reasonPhrase = response.getStatusLine().getReasonPhrase();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

            if (statusCode != 200) {
                log.error("[error] : " + result);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public JSONObject decodeFromIdToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

            if (payload != null) {
                return payload;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
