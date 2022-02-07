package net.mma.mmapromotion.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class FighterForm {
    private Long id;
    private String firstname;
    private String secondname;
    private String Nationality;
    private int age;
}
