package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//{
//        "message": "string"
//        }
@Getter
@Setter
@ToString
@Builder
public class ResponseMessageDTO {
    String message;

}
