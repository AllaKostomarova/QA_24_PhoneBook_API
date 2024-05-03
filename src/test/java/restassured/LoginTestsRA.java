package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.RequestBody;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.port;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class LoginTestsRA {
    String endpoint = "user/login/usernamepassword";
    @BeforeMethod
    public void precondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void loginSuccess(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("aa@aa.ru")
                .password("Test123$")
                .build();
        AuthResponseDTO responseDTO = given()
                .body(auth)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDTO.class);
        System.out.println("token ---> "+responseDTO.getToken());

    }

    @Test
    public void loginWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("aaaa.ru")
                .password("Test123$")
                .build();

        ErrorDTO errorDTO = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getMessage(),"Login or Password incorrect");
    }

    @Test
    public void loginWrongEmailFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("aaaa.ru")
                .password("Test123$")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }

    @Test
    public void loginEmptyEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("")
                .password("Test123$")
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("status", equalTo(401))
                .assertThat().body("error", equalTo("Unauthorized"))
                .assertThat().body("message", equalTo("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }

    @Test
    public void loginWrongPassword(){
        int i = (int)(System.currentTimeMillis()/1000)%3600;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("aa@aa.ru")
                .password("Password"+i)
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().body("status", equalTo(401))
                .assertThat().body("error", equalTo("Unauthorized"))
                .assertThat().body("message", equalTo("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }

    @Test
    public void loginUnregisteredUser(){
        int i = (int)(System.currentTimeMillis()/1000)%3600;

        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username(i+"unreg@user.ru")
                .password("Test123$")
                .build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().body("status", equalTo(401))
                .assertThat().body("error", equalTo("Unauthorized"))
                .assertThat().body("message", equalTo("Login or Password incorrect"))
                .assertThat().body("path", equalTo("/v1/user/login/usernamepassword"));
    }
}
