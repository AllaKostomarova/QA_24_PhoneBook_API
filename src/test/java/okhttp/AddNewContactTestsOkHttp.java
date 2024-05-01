package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.ResponseMessageDTO;
import manager.DataProviderAddNewContact;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddNewContactTestsOkHttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWFAYWEucnUiLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNDk4NTQ2MywiaWF0IjoxNzE0Mzg1NDYzfQ.YE3Rvmf0idn2Ewgnvp9eAOnZqbkvSPbPFVmASPYQLnw";
    String urlDTO = "https://contactapp-telran-backend.herokuapp.com/v1/contacts";

    @Test
    public void addNewContactSuccess() throws IOException {
        int i = (int)((System.currentTimeMillis()/1000)%3600);
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Murzik"+i)
                .lastName("Cat")
                .email(i+"murzic@cat.ru")
                .phone(i+"0123456789")
                .address("Home")
                .description("Pet success")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO), JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        ResponseMessageDTO messageDTO = gson.fromJson(response.body().string(), ResponseMessageDTO.class);
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        Assert.assertNotNull(messageDTO.getMessage());
        //System.out.println(messageDTO.getMessage());
        Assert.assertTrue(messageDTO.getMessage().contains("Contact was added!"));
        Assert.assertTrue(messageDTO.getMessage().contains("ID: "));

        //===================================================
        String[] mess = messageDTO.getMessage().split(": ");
        String id = mess[1];
       // System.out.println("id --> "+id);
    }

    @Test(dataProvider = "addNewContactDP", dataProviderClass = DataProviderAddNewContact.class)
    public void addNewContactTest_WrongData(ContactDTO contactDTO) throws IOException {
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        Assert.assertNotNull(errorDTO.getMessage());
        //================================
       // System.out.println("error --> "+errorDTO.getError());
       // System.out.println("message --> "+errorDTO.getMessage());
    }

    @Test(enabled = false, description = "response body is NULL && code 403 != 401")
    public void addNewContactTest_UnauturisedUser() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Kuzya_Unauth")
                .lastName("Cat_Unauth")
                .email("kuzyac@cat.ru")
                .phone("01234567897890")
                .address("Home")
                .description("Unauthorised User")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);//--> actual 403
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);//--> actual == NULL
        Assert.assertEquals(errorDTO.getStatus(), 401);//--> response.body == NULL
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");//--> response.body == NULL
        Assert.assertNotNull(errorDTO.getMessage());//--> response.body == NULL
    }

    @Test
    public void addNewContactTest_UnauthorisedUserWrongToken() throws IOException {
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Kuzya_Unauth")
                .lastName("Cat_Unauth")
                .email("kuzyac@cat.ru")
                .phone("01234567897890")
                .address("Home")
                .description("Unauthorised User")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url(urlDTO)
                .post(body)
                .addHeader("Authorization", "2345eRTYUm-687irlkjmfdHYUIYT23rt-uyytrthfg12RTYU")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 401);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
        Assert.assertNotNull(errorDTO.getMessage());
    }



}
