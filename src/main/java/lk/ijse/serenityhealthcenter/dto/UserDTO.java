package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String fullName;
    private Boolean isActive;
}