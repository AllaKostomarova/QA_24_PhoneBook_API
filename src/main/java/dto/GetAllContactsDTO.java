package dto;
//{
//        "contacts": [
//        {
//        "id": "string",
//        "name": "string",
//        "lastName": "string",
//        "email": "string",
//        "phone": "42606522459",
//        "address": "string",
//        "description": "string"
//        }
//        ]
//        }

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@Builder



public class GetAllContactsDTO {
    private List<ContactDTO> contacts;
}
