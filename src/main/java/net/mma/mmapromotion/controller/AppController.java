package net.mma.mmapromotion.controller;

import net.mma.mmapromotion.model.*;
import net.mma.mmapromotion.service.CardPostionService;
import net.mma.mmapromotion.service.FighterService;
import net.mma.mmapromotion.service.PPVService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController {

    private final PPVService ppvService;
    private final CardPostionService cardPostionService;
    private final FighterService fighterService;
    //private final Environment environment;


    @Autowired
    public AppController(PPVService ppvService, CardPostionService cardPostionService,
                         FighterService fighterService, Environment environment) {
        this.ppvService = ppvService;
        this.cardPostionService = cardPostionService;
        this.fighterService = fighterService;
        //this.environment = environment;
    }


    @GetMapping("/ppvs")
    public String findAll(Model model) {
        List<PPV> ppvs = ppvService.findAll();
        model.addAttribute("PPVs", ppvs);
        return "PPV-list";
    }

    @GetMapping("/PPV-create")
    public String createPPVForm(PPV ppv) {
        return "PPV-create";
    }

    /*
    @PostMapping("/PPV-create")
    public String createPPV(PPV ppv) {
        ppvService.savePPV(ppv);
        return "redirect:/ppvs";
    }
     */
    @ResponseBody
    @PostMapping(value ={"/PPV-create", "/PPV-update"})
    public ResponseEntity<?> createOrUpdatePPV(@RequestBody PPVForm formContent, Errors errors) {
        AjaxResponseBody response = new AjaxResponseBody();
        if (errors.hasErrors()) {
            response.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(0);
        response.setMsg("Check was successful. You are not supposed to see this message.");
        //response.setNextAddr("redirect:/ppvs");
        PPV formppv = new PPV();
        formppv.setId(formContent.getId());
        Long finder_id = formppv.getId();
        if (finder_id == null) {
            finder_id = -1L;
        } else {
            formppv = ppvService.findById(finder_id);
        }
        formppv.setDate(formContent.getDate());
        formppv.setName(formContent.getName());
        if (formppv.getName() == null || formppv.getName().length() < 1){
            response.setMsg("Unsuccessful! Name should not be empty!");
            response.setResult(1);
        } else {
            List<PPV> ppvs = ppvService.findByNameAndIdIsNot(formppv.getName(), finder_id);
            if (ppvs != null && ppvs.size() > 0) {
                response.setMsg("Unsuccessful! This name is already used by another entry!");
                response.setResult(1);
            } else {
                ppvService.savePPV(formppv);
            }
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/PPV-delete/{id}")
    public String deletePPV(@PathVariable("id") Long id) {
        ppvService.deleteById(id);
        return "redirect:/ppvs";
    }

    @GetMapping("/PPV-update/{id}")
    public String updatePPVForm(@PathVariable("id") Long id, Model model) {
        PPV ppv = ppvService.findById(id);
        model.addAttribute("PPV", ppv);
        return "PPV-update";
    }
    /*
    @PostMapping("/PPV-update")
    public String updatePPV(PPV ppv) {
        ppvService.savePPV(ppv);
        return "redirect:/ppvs";
    }
     */


    @GetMapping("/cards_{ppv_id}")
    public String findAllRelatedCards(Model model, @PathVariable("ppv_id") Long ppv_id) {
        List<CardPostion> cards = cardPostionService.findAllRelated(ppv_id);
        model.addAttribute("cards", cards);
        model.addAttribute("ref_id", ppv_id);
        return "cards-list";
    }

    @GetMapping("/card-create_{ppv_id}")
    public String createCardPostionForm(Model model, CardPostion cardPostion, @PathVariable("ppv_id") Long ppv_id) {
        model.addAttribute("ref_id", ppv_id);
        cardPostion.setPpv(ppvService.findById(ppv_id));
        return "card-create";
    }

    /*
    @PostMapping("/card-create_{ppv_id}")
    public String createCardPostion(Model model, CardPostion cardPostion, @PathVariable("ppv_id") Long ppv_id) {
        cardPostion.setPpv(ppvService.findById(ppv_id));
        cardPostionService.saveCardPostion(cardPostion);
        return "redirect:/cards_" + cardPostion.getPpv().getId();
    }
     */

    @ResponseBody
    @PostMapping(value ={"/card-create_{ppv_id}", "/card-update_{ppv_id}"})
    public ResponseEntity<?> createOrUpdateCardPostion(@RequestBody CardPostionForm formContent, Errors errors,
                                                       @PathVariable("ppv_id") Long ppv_id) {
        AjaxResponseBody response = new AjaxResponseBody();
        if (errors.hasErrors()) {
            response.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(0);
        response.setMsg("Check was successful. You are not supposed to see this message.");
        //response.setNextAddr("/cards_" + ppv_id);
        CardPostion formcard = new CardPostion();
        formcard.setId(formContent.getId());
        Long finder_id = formcard.getId();
        if (finder_id == null) {
            finder_id = -1L;
            formcard.setPpv(ppvService.findById(ppv_id));
        } else {
            formcard = cardPostionService.findById(finder_id);
        }
        formcard.setNumberinshow(formContent.getNumberinshow());
        formcard.setLength(formContent.getLength());
        formcard.setTitlename(formContent.getTitlename());
        formcard.setWinner(formContent.getWinner());
        if (formcard.getTitlename() == null || formcard.getTitlename().length() < 1){
            response.setMsg("Unsuccessful! Title name should not be empty!");
            response.setResult(1);
        }
        else if (formcard.getWinner() == null || formcard.getWinner().length() < 1){
            response.setMsg("Unsuccessful! Winner should not be empty!");
            response.setResult(1);
        }
        else if (formcard.getNumberinshow() < 1){
            response.setMsg("Unsuccessful! Number in show should be positive!");
            response.setResult(1);
        } else if (formcard.getLength() < 1){
            response.setMsg("Unsuccessful! Length should be positive!");
            response.setResult(1);
        } else {
            List<CardPostion> cards = cardPostionService.findByPpv_idAndNumberinshowAndIdIsNot(ppv_id, formcard.getNumberinshow(), finder_id);
            if (cards != null && cards.size() > 0) {
                response.setMsg("Unsuccessful! Two entries cannot be designated the same number during one show!");
                response.setResult(1);
            } else {
                cardPostionService.saveCardPostion(formcard);
            }
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/card-delete/{id}")
    public String deleteCardPostion(@PathVariable("id") Long id) {
        Long ppv_id = cardPostionService.findById(id).getPpv().getId();
        cardPostionService.deleteById(id);
        return "redirect:/cards_" + ppv_id;
    }

    @GetMapping("/card-update/{id}")
    public String updateCardPostionForm(Model model, @PathVariable("id") Long id) {
        CardPostion cardPostion = cardPostionService.findById(id);
        PPV ppv = cardPostion.getPpv();
        model.addAttribute("ref_id", ppv.getId());
        model.addAttribute("cardPostion", cardPostion);
        return "card-update";
    }

    /*
    @PostMapping("/card-update_{ppv_id}")
    public String updateCardPostion(Model model, CardPostion cardPostion, @PathVariable("ppv_id") Long ppv_id) {
        CardPostion result = cardPostionService.findById(cardPostion.getId());
        result.setLength(cardPostion.getLength());
        result.setNumberinshow(cardPostion.getNumberinshow());
        result.setWinner(cardPostion.getWinner());
        result.setTitle_name(cardPostion.getTitle_name());
        cardPostionService.saveCardPostion(result);
        return "redirect:/cards_" + result.getPpv().getId();
    }
    */

    @GetMapping("/fighters_{card_id}")
    public String findAllRelatedFighters(Model model, @PathVariable("card_id") Long card_id) {
        List<Fighter> fighters = fighterService.findAllRelated(card_id);
        model.addAttribute("fighters", fighters);
        model.addAttribute("ref_id", card_id);
        model.addAttribute("ppv_id", cardPostionService.findById(card_id).getPpv().getId());
        return "fighters-list";
    }

    @GetMapping("/fighter-create_{card_id}")
    public String createFighterForm(Model model, Fighter fighter, @PathVariable("card_id") Long card_id) {
        model.addAttribute("ref_id", card_id);
        fighter.setCardpostion(cardPostionService.findById(card_id));
        return "fighter-create";
    }

    @ResponseBody
    @PostMapping(value ={"/fighter-create_{card_id}", "/fighter-update_{card_id}"})
    public ResponseEntity<?> createOrUpdateFighter(@RequestBody FighterForm formContent, Errors errors,
                                                       @PathVariable("card_id") Long card_id) {
        AjaxResponseBody response = new AjaxResponseBody();
        if (errors.hasErrors()) {
            response.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(0);
        response.setMsg("Check was successful. You are not supposed to see this message.");
        //response.setNextAddr("/fighters_" + card_id);
        Fighter formfighter = new Fighter();
        formfighter.setId(formContent.getId());
        Long finder_id = formfighter.getId();
        if (finder_id == null) {
            finder_id = -1L;
            formfighter.setCardpostion(cardPostionService.findById(card_id));
        } else {
            formfighter = fighterService.findById(finder_id);
        }
        formfighter.setFirstname(formContent.getFirstname());
        formfighter.setSecondname(formContent.getSecondname());
        formfighter.setNationality(formContent.getNationality());
        formfighter.setAge(formContent.getAge());
        if (formfighter.getFirstname() == null || formfighter.getFirstname().length() < 1){
            response.setMsg("Unsuccessful! Names should not be empty!");
            response.setResult(1);
        }
        else if (formfighter.getSecondname() == null || formfighter.getSecondname().length() < 1){
            response.setMsg("Unsuccessful! Names should not be empty!");
            response.setResult(1);
        }
        else if (formfighter.getNationality() == null || formfighter.getNationality().length() < 1){
            response.setMsg("Unsuccessful! Nationality should not be empty!");
            response.setResult(1);
        } else if (formfighter.getAge() < 1){
            response.setMsg("Unsuccessful! Age should be positive!");
            response.setResult(1);
        } else {
            List<Fighter> fighters = fighterService.findByFirstnameAndSecondnameAndCardpostion_IdIsNot(formfighter.getFirstname(),
                    formfighter.getSecondname(), finder_id);
            if (fighters != null && fighters.size() > 0) {
                response.setMsg("Unsuccessful! Same fighter cannot be mentioned twice in one match!");
                response.setResult(1);
            } else {
                fighterService.saveFighter(formfighter);
            }
        }
        return ResponseEntity.ok(response);
    }


    /*
    @PostMapping("/fighter-create_{card_id}")
    public String createFighter(Model model, Fighter fighter, @PathVariable("card_id") Long card_id) {
        fighter.setCardpostion(cardPostionService.findById(card_id));
        fighterService.saveFighter(fighter);
        return "redirect:/fighters_" + fighter.getCardpostion().getId();
    }
     */

    @GetMapping("/fighter-delete/{id}")
    public String deleteFighter(@PathVariable("id") Long id) {
        Long card_id = fighterService.findById(id).getCardpostion().getId();
        fighterService.deleteById(id);
        return "redirect:/fighters_" + card_id;
    }

    @GetMapping("/fighter-update/{id}")
    public String updateFighterForm(Model model, @PathVariable("id") Long id) {
        Fighter fighter = fighterService.findById(id);
        CardPostion card = fighter.getCardpostion();
        model.addAttribute("ref_id", card.getId());
        model.addAttribute("fighter", fighter);
        return "fighter-update";
    }

    /*
    @PostMapping("/fighter-update_{card_id}")
    public String updateFighter(Model model, Fighter fighter, @PathVariable("card_id") Long card_id) {
        fighter.setCardpostion(cardPostionService.findById(card_id));
        fighterService.saveFighter(fighter);
        return "redirect:/fighters_" + fighter.getCardpostion().getId();
    }
     */
}