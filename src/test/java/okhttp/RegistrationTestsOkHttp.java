package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegistrationTestsOkHttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String urlDTO = "https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword";

    @Test
    public void registrationSuccess() throws IOException {
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDTO authDTO = AuthRequestDTO.builder()
                .username(i+"auth@username.ru")
                .password("Test123$")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(authDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        Assert.assertNotNull(responseDTO.getToken());
    }

    @Test
    public void registrationTestWrongEmail() throws IOException {
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDTO authDTO = AuthRequestDTO.builder()
                .username(i+"wrong@@mail")
                .password("Valid098!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(authDTO), JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage().toString(), "{username=must be a well-formed email address}");
    }

    @Test
    public void registrationTestWrongPassword() throws IOException {
        int i = (int) ((System.currentTimeMillis()/1000)%3600);
        AuthRequestDTO authDTO = AuthRequestDTO.builder()
                .username(i+"wrong@passwod.ru")
                .password("123$@!qvo")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(authDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        System.out.println(errorDTO.getMessage().toString());
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertEquals(errorDTO.getMessage().toString(), "{password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]}");
    }

    @Test
    public void registrationTestAlreadyRegisteredUser() throws IOException {
        AuthRequestDTO authDTO = AuthRequestDTO.builder()
                .username("x@x.ru")
                .password("Test123$")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(authDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 409);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 409);
        System.out.println(errorDTO);
        Assert.assertEquals(errorDTO.getError(), "Conflict");
        Assert.assertEquals(errorDTO.getMessage().toString(), "User already exists");

    }
}
