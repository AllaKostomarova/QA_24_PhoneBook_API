package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDTO;
import dto.GetAllContactsDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String endpoint = "contacts";
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWFAYWEucnUiLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNDk4NTQ2MywiaWF0IjoxNzE0Mzg1NDYzfQ.YE3Rvmf0idn2Ewgnvp9eAOnZqbkvSPbPFVmASPYQLnw";

    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void getAllContactsSuccess(){
        GetAllContactsDTO contactsDTO = given()
                .header("Authorization", token)
                .when()
                .get(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDTO.class);
        List<ContactDTO> list = contactsDTO.getContacts();
        System.out.println("Size of list---> "+list.size());
        for (ContactDTO contact: list){
            System.out.println(contact.getId());
            System.out.println(contact.getEmail());
        }
    }
}
