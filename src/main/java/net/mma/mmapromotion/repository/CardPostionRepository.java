package net.mma.mmapromotion.repository;

import net.mma.mmapromotion.model.CardPostion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardPostionRepository extends JpaRepository<CardPostion, Long> {
    List<CardPostion> findByppv_id(Long id);
    List<CardPostion> findByPpv_idAndNumberinshowAndIdIsNot(Long ppv_id, int number, Long id);
}

