package net.mma.mmapromotion.service;

import net.mma.mmapromotion.model.PPV;
import net.mma.mmapromotion.repository.PPVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PPVService {

    private final PPVRepository ppvRepository;

    @Autowired
    public PPVService(PPVRepository ppvRepository) {
        this.ppvRepository = ppvRepository;
    }


    public PPV findById(Long id) {
        return ppvRepository.findById(id).orElse(null);
    }


    public List<PPV> findAll() {
        return ppvRepository.findAll();
    }

    public PPV savePPV (PPV ppv) {
        return ppvRepository.save(ppv);
    }

    public void deleteById (Long id) {
        ppvRepository.deleteById(id);
    }

    public List<PPV> findByNameAndIdIsNot(String name, Long id) {
        return ppvRepository.findByNameAndIdIsNot(name, id);
    }
}
