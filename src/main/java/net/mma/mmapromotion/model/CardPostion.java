package net.mma.mmapromotion.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "cardpostion")
public class CardPostion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = PPV.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PPV_id")
    private PPV ppv;
    @Column(name = "number_in_show")
    private int numberinshow;
    @Column(name = "Length")
    private int length;
    @Column(name = "title_name")
    private String titlename;
    @Column(name = "Winner")
    private String winner;

    @OneToMany(mappedBy = "cardpostion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Fighter> fighters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardPostion )) return false;
        return id != null && id.equals(((CardPostion) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return id.toString() + ppv.getId().toString() +
                numberinshow + length + titlename + winner;
    }
}
