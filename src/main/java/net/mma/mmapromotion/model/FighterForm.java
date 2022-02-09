package net.mma.mmapromotion.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class FighterForm {
    private Long id;
    private String firstName;
    private String secondName;
    private String nationality;
    private int age;
}
