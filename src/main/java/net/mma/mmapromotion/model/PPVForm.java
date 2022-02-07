package net.mma.mmapromotion.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PPVForm {

    //private PPV ppv;
    private String name;
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;
    private Long id;
}
