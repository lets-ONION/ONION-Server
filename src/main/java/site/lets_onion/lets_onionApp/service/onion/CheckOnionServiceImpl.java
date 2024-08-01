package site.lets_onion.lets_onionApp.service.onion;


import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import site.lets_onion.lets_onionApp.domain.onion.NegCheckResult;
import site.lets_onion.lets_onionApp.domain.onion.PosCheckResult;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CheckOnionServiceImpl implements CheckOnionService {

    private String API_KEY = ""; //숨겨야함
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";


    @Transactional
    public ResponseDTO<PosCheckResult> checkPosNote(String posNote) {
        String gptResponse = getGptResponse(posNote);
        switch (gptResponse) {
            case "0":
                return new ResponseDTO<>(PosCheckResult.POSITIVE, Responses.OK);
            case "1":
                return new ResponseDTO<>(PosCheckResult.NEGATIVE, Responses.OK);
            case "2":
                return new ResponseDTO<>(PosCheckResult.INVALID, Responses.OK);
            default:
                return new ResponseDTO<>(PosCheckResult.FAIL, Responses.OK);
        }
    }

    @Transactional
    public ResponseDTO<NegCheckResult> checkNegNote(String negNote) {
        String gptResponse = getGptResponse(negNote);
        switch (gptResponse) {
            case "0":
                return new ResponseDTO<>(NegCheckResult.VALID, Responses.OK);
            case "1":
                return new ResponseDTO<>(NegCheckResult.VALID, Responses.OK);
            case "2":
                return new ResponseDTO<>(NegCheckResult.INVALID, Responses.OK);
            default:
                return new ResponseDTO<>(NegCheckResult.FAIL, Responses.OK);
        }
    }


    public String getGptResponse(String note) {
        String question = note
                + """
                \n위의 텍스트의 결론이 문맥상으로 긍정적인지, 부정적인지, 혹은 판단할 수 없는지 확인해줘.
                \n만약 문맥상 100% 부정적이라면 부정으로 판단해줘. 그렇지 않으면 긍정으로 판단해줘.
                \n긍정적이면 0, 부정적이면 1을 보내줘. 만약 텍스트가 너무 짧거나, 텍스트가 불명확하거나, 텍스트가 인식되지 않거나, 판단이 어려우면 2를 보내줘. 
                나머지 경우에는 반드시 긍정 혹은 부정 중 하나로 판단해줘. 다른 답은 하지 말아줘.
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model","gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", question)
        ));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(ENDPOINT, HttpMethod.POST, requestEntity, Map.class);
        // 응답 본문에서 생성된 응답 추출하기
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
        }
        return "No response found";
    }

}
