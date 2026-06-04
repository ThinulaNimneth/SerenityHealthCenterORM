package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProgramDto {
    private String programId;
    private String name;
    private String duration;
    private String fee;
}
