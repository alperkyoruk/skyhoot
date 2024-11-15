package skylab.skyhoot.WebAPI.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.AnswerOptionService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.entities.AnswerOption;
import skylab.skyhoot.entities.DTOs.AnswerOption.CreateAnswerOptionDto;
import skylab.skyhoot.entities.DTOs.AnswerOption.GetAnswerOptionDto;

import java.util.List;

@RestController
@RequestMapping("/api/answerOptions")
public class AnswerOptionController {
    private AnswerOptionService answerOptionService;

    public AnswerOptionController(AnswerOptionService answerOptionService){
        this.answerOptionService = answerOptionService;
    }

    @PostMapping("/addAnswerOption")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN  ', 'ROLE_MODERATOR', 'ROLE_USER')")
    public Result addAnswerOption(@RequestBody CreateAnswerOptionDto createAnswerOptionDto){
        return answerOptionService.addAnswerOption(createAnswerOptionDto);
    }

    @PostMapping("/deleteAnswerOption")
    public Result deleteAnswerOption(@RequestBody int answerOptionId){
        return answerOptionService.deleteAnswerOption(answerOptionId);
    }

    @PostMapping("/updateAnswerOption")
    public Result updateAnswerOption(@RequestBody GetAnswerOptionDto getAnswerOptionDto){
        return answerOptionService.updateAnswerOption(getAnswerOptionDto);
    }

    @GetMapping("/getAnswerOptionById")
    public DataResult<GetAnswerOptionDto> getAnswerOptionById(@RequestBody int answerOptionId){
        return answerOptionService.getAnswerOptionById(answerOptionId);
    }

    @GetMapping("/getAnswerOptionsByQuestionId")
    public DataResult<List<GetAnswerOptionDto>> getAnswerOptionsByQuestionId(@RequestBody int questionId){
        return answerOptionService.getAnswerOptionsByQuestionId(questionId);
    }

    @GetMapping("/getAnswerOptionEntityById")
    public DataResult<AnswerOption> getAnswerOptionEntityById(@RequestBody int answerOptionId){
        return answerOptionService.getAnswerOptionEntityById(answerOptionId);
    }


}
