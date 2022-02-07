package net.mma.mmapromotion.repository;

import net.mma.mmapromotion.model.PPV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PPVRepository extends JpaRepository<PPV, Long> {
    List<PPV> findByNameAndIdIsNot(String name, Long id);
}
