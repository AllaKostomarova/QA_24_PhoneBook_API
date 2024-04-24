package dto;

//{
//   "username": "string",
//    "password": "string"
//  }

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class AuthRequestDTO {
    private String username;
    private String password;
}
