package dto;

//{
//        "timestamp": "2024-04-21T15:16:25.041Z",
//        "status": 0,
//        "error": "string",
//        "message": {},
//        "path": "string"
//        }

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class ErrorDTO {
    private int status;
    private String error;
    private Object message;
    private String path;



}
