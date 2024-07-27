package site.lets_onion.lets_onionApp.dto.push;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import lombok.Data;

import java.util.List;

@Data
public class PushTestResponseDTO {

    List<SendResponse> responses;
    int successCount;
    int failureCount;

    public PushTestResponseDTO(BatchResponse batchResponse) {
        this.responses = batchResponse.getResponses();
        this.successCount = batchResponse.getSuccessCount();
        this.failureCount = batchResponse.getFailureCount();
    }
}
