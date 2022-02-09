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
        PPV formPpv = new PPV();
        formPpv.setId(formContent.getId());
        Long finderId = formPpv.getId();
        if (finderId == null) {
            finderId = -1L;
        } else {
            formPpv = ppvService.findById(finderId);
        }
        formPpv.setDate(formContent.getDate());
        formPpv.setName(formContent.getName());
        if (formPpv.getName() == null || formPpv.getName().length() < 1){
            response.setMsg("Unsuccessful! Name should not be empty!");
            response.setResult(1);
        } else {
            List<PPV> ppvs = ppvService.findByNameAndIdIsNot(formPpv.getName(), finderId);
            if (ppvs != null && ppvs.size() > 0) {
                response.setMsg("Unsuccessful! This name is already used by another entry!");
                response.setResult(1);
            } else {
                ppvService.savePPV(formPpv);
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


    @GetMapping("/cards_{ppvId}")
    public String findAllRelatedCards(Model model, @PathVariable("ppvId") Long ppvId) {
        List<CardPostion> cards = cardPostionService.findAllRelated(ppvId);
        model.addAttribute("cards", cards);
        model.addAttribute("refId", ppvId);
        return "cards-list";
    }

    @GetMapping("/card-create_{ppvId}")
    public String createCardPostionForm(Model model, CardPostion cardPostion, @PathVariable("ppvId") Long ppvId) {
        model.addAttribute("refId", ppvId);
        cardPostion.setPpv(ppvService.findById(ppvId));
        return "card-create";
    }

    /*
    @PostMapping("/card-create_{ppvId}")
    public String createCardPostion(Model model, CardPostion cardPostion, @PathVariable("ppvId") Long ppvId) {
        cardPostion.setPpv(ppvService.findById(ppvId));
        cardPostionService.saveCardPostion(cardPostion);
        return "redirect:/cards_" + cardPostion.getPpv().getId();
    }
     */

    @ResponseBody
    @PostMapping(value ={"/card-create_{ppvId}", "/card-update_{ppvId}"})
    public ResponseEntity<?> createOrUpdateCardPostion(@RequestBody CardPostionForm formContent, Errors errors,
                                                       @PathVariable("ppvId") Long ppvId) {
        AjaxResponseBody response = new AjaxResponseBody();
        if (errors.hasErrors()) {
            response.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(0);
        response.setMsg("Check was successful. You are not supposed to see this message.");
        //response.setNextAddr("/cards_" + ppvId);
        CardPostion formCard = new CardPostion();
        formCard.setId(formContent.getId());
        Long finderId = formCard.getId();
        if (finderId == null) {
            finderId = -1L;
            formCard.setPpv(ppvService.findById(ppvId));
        } else {
            formCard = cardPostionService.findById(finderId);
        }
        formCard.setNumberInShow(formContent.getNumberInShow());
        formCard.setLength(formContent.getLength());
        formCard.setTitleName(formContent.getTitleName());
        formCard.setWinner(formContent.getWinner());
        if (formCard.getTitleName() == null || formCard.getTitleName().length() < 1){
            response.setMsg("Unsuccessful! Title name should not be empty!");
            response.setResult(1);
        }
        else if (formCard.getWinner() == null || formCard.getWinner().length() < 1){
            response.setMsg("Unsuccessful! Winner should not be empty!");
            response.setResult(1);
        }
        else if (formCard.getNumberInShow() < 1){
            response.setMsg("Unsuccessful! Number in show should be positive!");
            response.setResult(1);
        } else if (formCard.getLength() < 1){
            response.setMsg("Unsuccessful! Length should be positive!");
            response.setResult(1);
        } else {
            List<CardPostion> cards = cardPostionService.findByPpv_idAndNumberInShowAndIdIsNot(ppvId, formCard.getNumberInShow(), finderId);
            if (cards != null && cards.size() > 0) {
                response.setMsg("Unsuccessful! Two entries cannot be designated the same number during one show!");
                response.setResult(1);
            } else {
                cardPostionService.saveCardPostion(formCard);
            }
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/card-delete/{id}")
    public String deleteCardPostion(@PathVariable("id") Long id) {
        Long ppvId = cardPostionService.findById(id).getPpv().getId();
        cardPostionService.deleteById(id);
        return "redirect:/cards_" + ppvId;
    }

    @GetMapping("/card-update/{id}")
    public String updateCardPostionForm(Model model, @PathVariable("id") Long id) {
        CardPostion cardPostion = cardPostionService.findById(id);
        PPV ppv = cardPostion.getPpv();
        model.addAttribute("refId", ppv.getId());
        model.addAttribute("cardPostion", cardPostion);
        return "card-update";
    }

    /*
    @PostMapping("/card-update_{ppvId}")
    public String updateCardPostion(Model model, CardPostion cardPostion, @PathVariable("ppvId") Long ppvId) {
        CardPostion result = cardPostionService.findById(cardPostion.getId());
        result.setLength(cardPostion.getLength());
        result.setNumberInShow(cardPostion.getNumberInShow());
        result.setWinner(cardPostion.getWinner());
        result.setTitleName(cardPostion.getTitleName());
        cardPostionService.saveCardPostion(result);
        return "redirect:/cards_" + result.getPpv().getId();
    }
    */

    @GetMapping("/fighters_{cardId}")
    public String findAllRelatedFighters(Model model, @PathVariable("cardId") Long cardId) {
        List<Fighter> fighters = fighterService.findAllRelated(cardId);
        model.addAttribute("fighters", fighters);
        model.addAttribute("refId", cardId);
        model.addAttribute("ppvId", cardPostionService.findById(cardId).getPpv().getId());
        return "fighters-list";
    }

    @GetMapping("/fighter-create_{cardId}")
    public String createFighterForm(Model model, Fighter fighter, @PathVariable("cardId") Long cardId) {
        model.addAttribute("refId", cardId);
        fighter.setCardPostion(cardPostionService.findById(cardId));
        return "fighter-create";
    }

    @ResponseBody
    @PostMapping(value ={"/fighter-create_{cardId}", "/fighter-update_{cardId}"})
    public ResponseEntity<?> createOrUpdateFighter(@RequestBody FighterForm formContent, Errors errors,
                                                       @PathVariable("cardId") Long cardId) {
        AjaxResponseBody response = new AjaxResponseBody();
        if (errors.hasErrors()) {
            response.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(response);
        }
        response.setResult(0);
        response.setMsg("Check was successful. You are not supposed to see this message.");
        //response.setNextAddr("/fighters_" + cardId);
        Fighter formFighter = new Fighter();
        formFighter.setId(formContent.getId());
        Long finderId = formFighter.getId();
        if (finderId == null) {
            finderId = -1L;
            formFighter.setCardPostion(cardPostionService.findById(cardId));
        } else {
            formFighter = fighterService.findById(finderId);
        }
        formFighter.setFirstName(formContent.getFirstName());
        formFighter.setSecondName(formContent.getSecondName());
        formFighter.setNationality(formContent.getNationality());
        formFighter.setAge(formContent.getAge());
        if (formFighter.getFirstName() == null || formFighter.getFirstName().length() < 1){
            response.setMsg("Unsuccessful! Names should not be empty!");
            response.setResult(1);
        }
        else if (formFighter.getSecondName() == null || formFighter.getSecondName().length() < 1){
            response.setMsg("Unsuccessful! Names should not be empty!");
            response.setResult(1);
        }
        else if (formFighter.getNationality() == null || formFighter.getNationality().length() < 1){
            response.setMsg("Unsuccessful! Nationality should not be empty!");
            response.setResult(1);
        } else if (formFighter.getAge() < 1){
            response.setMsg("Unsuccessful! Age should be positive!");
            response.setResult(1);
        } else {
            List<Fighter> fighters = fighterService.findByFirstNameAndSecondNameAndCardPostion_IdIsNot(formFighter.getFirstName(),
                    formFighter.getSecondName(), finderId);
            if (fighters != null && fighters.size() > 0) {
                response.setMsg("Unsuccessful! Same fighter cannot be mentioned twice in one match!");
                response.setResult(1);
            } else {
                fighterService.saveFighter(formFighter);
            }
        }
        return ResponseEntity.ok(response);
    }


    /*
    @PostMapping("/fighter-create_{cardId}")
    public String createFighter(Model model, Fighter fighter, @PathVariable("cardId") Long cardId) {
        fighter.setCardPostion(cardPostionService.findById(cardId));
        fighterService.saveFighter(fighter);
        return "redirect:/fighters_" + fighter.getCardPostion().getId();
    }
     */

    @GetMapping("/fighter-delete/{id}")
    public String deleteFighter(@PathVariable("id") Long id) {
        Long cardId = fighterService.findById(id).getCardPostion().getId();
        fighterService.deleteById(id);
        return "redirect:/fighters_" + cardId;
    }

    @GetMapping("/fighter-update/{id}")
    public String updateFighterForm(Model model, @PathVariable("id") Long id) {
        Fighter fighter = fighterService.findById(id);
        CardPostion card = fighter.getCardPostion();
        model.addAttribute("refId", card.getId());
        model.addAttribute("fighter", fighter);
        return "fighter-update";
    }

    /*
    @PostMapping("/fighter-update_{cardId}")
    public String updateFighter(Model model, Fighter fighter, @PathVariable("cardId") Long cardId) {
        fighter.setCardPostion(cardPostionService.findById(cardId));
        fighterService.saveFighter(fighter);
        return "redirect:/fighters_" + fighter.getCardPostion().getId();
    }
     */
}