package manager;

import dto.ContactDTO;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataProviderAddNewContact {
    @DataProvider
    public Iterator<Object[]> addNewContactDP() throws IOException {
        List<Object[]> contacts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/wrongcontacts.csv")));
        String line = reader.readLine();
        while (line !=null){
            String[] data = line.split(",");
            contacts.add(new Object[]{ContactDTO.builder()
                    .name(data[0])
                    .lastName(data[1])
                    .email(data[2])
                    .phone(data[3])
                    .address(data[4])
                    .description(data[5])
                    .build()});
            line = reader.readLine();
        }
//        contacts.add(new Object[]{ContactDTO.builder()
//                .name("Cola")
//                .lastName("Coca-Cola")
//                .email("cola.cola")
//                .phone("7777777777777")
//                .address("house")
//                .description("wrong email")
//                .build()});
        return contacts.iterator();
    }
}
