package net.mma.mmapromotion.model;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ppv")
public class PPV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date Date;
    @OneToMany(mappedBy = "ppv", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardPostion> cards;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PPV )) return false;
        return id != null && id.equals(((PPV) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return id.toString() + name.toString() + Date.toString();
    }
}
