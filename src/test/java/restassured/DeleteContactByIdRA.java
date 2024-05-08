package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {
    String endpoint = "contacts/";
    String id;
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWFAYWEucnUiLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNDk4NTQ2MywiaWF0IjoxNzE0Mzg1NDYzfQ.YE3Rvmf0idn2Ewgnvp9eAOnZqbkvSPbPFVmASPYQLnw";

    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
        int i = (int)((System.currentTimeMillis()/1000)%3600);
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Murzik"+i)
                .lastName("Cat")
                .email(i+"murzic@cat.ru")
                .phone(i+"0123456789")
                .address("Home")
                .description("Pet success")
                .build();

        String message = given()
                .body(contactDTO)
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");
        String[] all = message.split(": ");
        String id = all[1];
    }

    @Test
    public void deleteContactByIdSuccess(){
        given()
                .header("Authorization", token)
                .when()
                .delete(endpoint+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was deleted!"));
    }

}
