package net.mma.mmapromotion.service;

import net.mma.mmapromotion.model.Fighter;
import net.mma.mmapromotion.repository.FighterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FighterService {

    private final FighterRepository fighterRepository;

    @Autowired
    public FighterService(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }
    public Fighter findById(Long id) {
        return fighterRepository.findById(id).orElse(null);
    }

    public List<Fighter> findAll() {
        return fighterRepository.findAll();
    }

    public List<Fighter> findAllRelated(long id) {
        return fighterRepository.findByCardPostion_id(id);
    }

    public Fighter saveFighter (Fighter cardPostion) {
        return fighterRepository.save(cardPostion);
    }

    public void deleteById (Long id) {
        fighterRepository.deleteById(id);
    }

    public List<Fighter> findByFirstNameAndSecondNameAndCardPostion_IdIsNot(String fname, String sname, Long id){
        return  fighterRepository.findByFirstNameAndSecondNameAndCardPostion_IdIsNot(fname, sname, id);
    }

}
