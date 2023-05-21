package com.inn.restaurant.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;
    private String name;
    private String email;
    private String telephoneNumber;
    private String status;

    public UserWrapper(Integer id, String name, String email, String telephoneNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.status = status;
    }
}
