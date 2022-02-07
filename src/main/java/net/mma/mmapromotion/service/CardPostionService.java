package net.mma.mmapromotion.service;

import net.mma.mmapromotion.model.CardPostion;
import net.mma.mmapromotion.repository.CardPostionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardPostionService {
    private final CardPostionRepository cardPostionRepository;

    @Autowired
    public CardPostionService(CardPostionRepository cardPostionRepository) {
        this.cardPostionRepository = cardPostionRepository;
    }
    public CardPostion findById(Long id) {
        return cardPostionRepository.findById(id).orElse(null);
    }

    public List<CardPostion> findAll() {
        return cardPostionRepository.findAll();
    }

    public List<CardPostion> findAllRelated(long id) {
        return cardPostionRepository.findByppv_id(id);
    }

    public CardPostion saveCardPostion (CardPostion cardPostion) {
        return cardPostionRepository.save(cardPostion);
    }

    public void deleteById (Long id) {
        cardPostionRepository.deleteById(id);
    }

    public List<CardPostion> findByPpv_idAndNumberinshowAndIdIsNot(Long ppv_id, int number, Long id){
        return cardPostionRepository.findByPpv_idAndNumberinshowAndIdIsNot(ppv_id, number, id);
    }
}
