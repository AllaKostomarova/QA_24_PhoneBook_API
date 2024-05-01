package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.ResponseMessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteContactByIDOkhttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWFAYWEucnUiLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNDk4NTQ2MywiaWF0IjoxNzE0Mzg1NDYzfQ.YE3Rvmf0idn2Ewgnvp9eAOnZqbkvSPbPFVmASPYQLnw";
    String urlDTOWithoutID = "https://contactapp-telran-backend.herokuapp.com/v1/contacts/";
    String id;

    @BeforeMethod
    public void precondition_addNewContact() throws IOException {
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
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        if(response.code() == 200) {
            ResponseMessageDTO messageDTO = gson.fromJson(response.body().string(), ResponseMessageDTO.class);
            System.out.println("message --> " + messageDTO.getMessage());
            //===================================================
            String[] mess = messageDTO.getMessage().split(": ");
            id = mess[1];
            System.out.println("ID ---> " + id);
        }
    }

    @Test
    public void deleteContactByIDSuccess() throws IOException {
        Request request = new Request.Builder()
                .url(urlDTOWithoutID+id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);
        ResponseMessageDTO responseMessageDTO = gson.fromJson(response.body().string(), ResponseMessageDTO.class);
        System.out.println(responseMessageDTO.getMessage());
        Assert.assertEquals(responseMessageDTO.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIDSuccess_wrongToken() throws IOException {
        Request request = new Request.Builder()
                .url(urlDTOWithoutID+id)
                .delete()
                .addHeader("Authorization", "token345YUI-7686jjhhjfh-wringOZ2")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
        //System.out.println("mess---> "+errorDTO.toString());
    }

    @Test
    public void deleteContactByIDSuccess_wrongID_notFound() throws IOException {
        Request request = new Request.Builder()
                .url(urlDTOWithoutID+"123456789rty000-rt-67-er")
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        System.out.println("mess---> "+errorDTO.toString());
    }




}
