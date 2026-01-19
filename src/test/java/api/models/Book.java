package api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
//@NoArgsConstructor
public class Book {
    String userId;
    @JsonProperty("collectionOfIsbns")
    List<ISBN> collectionOfIsbns;

    @AllArgsConstructor
    @Getter
     public static class ISBN {
         String isbn;
    }
}
