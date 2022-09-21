package com.training.data_storage.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Component
public class City implements Serializable {

    private static final long serialVersionUID = 3906771677381811334L;

    @NonNull
    private String id;

    @NonNull
    private String name;

    private String countryCode;

    private String district;

    private String population;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(this);
    }
}



