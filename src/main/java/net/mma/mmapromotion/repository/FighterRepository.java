package net.mma.mmapromotion.repository;


import net.mma.mmapromotion.model.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FighterRepository extends JpaRepository<Fighter, Long>{
    List<Fighter> findByCardPostion_id(Long id);
    List<Fighter> findByFirstNameAndSecondNameAndCardPostion_IdIsNot(String fname, String sname, Long id);
}