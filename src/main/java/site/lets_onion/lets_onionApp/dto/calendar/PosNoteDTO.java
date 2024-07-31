package site.lets_onion.lets_onionApp.dto.calendar;

import lombok.Data;

@Data
public class PosNoteDTO {

    private String note;

    public PosNoteDTO(String note) {
        this.note = note;
    }
}
