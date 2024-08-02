package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteRequestDTO {

    @JsonProperty("note")
    private String note;

    @JsonCreator
    public NoteRequestDTO(String note) {
        this.note = note;
    }

}
