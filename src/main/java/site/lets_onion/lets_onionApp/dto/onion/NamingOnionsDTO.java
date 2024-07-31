package site.lets_onion.lets_onionApp.dto.onion;

import lombok.Data;

@Data
public class NamingOnionsDTO {

    private String posOnionName;
    private String negOnionName;

    public NamingOnionsDTO(String posOnionName, String negOnionName) {
        this.posOnionName = posOnionName;
        this.negOnionName = negOnionName;
    }
}
