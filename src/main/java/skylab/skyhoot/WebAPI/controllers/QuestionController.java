package skylab.skyhoot.WebAPI.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import skylab.skyhoot.Business.abstracts.QuestionService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.Result;
import skylab.skyhoot.core.result.SuccessResult;
import skylab.skyhoot.entities.DTOs.Question.CreateQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionDto;
import skylab.skyhoot.entities.DTOs.Question.GetQuestionForHostDto;
import skylab.skyhoot.entities.Question;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private QuestionService questionService;
    private final SimpMessagingTemplate messagingTemplate;

    public QuestionController(QuestionService questionService, SimpMessagingTemplate simpMessagingTemplate) {
        this.questionService = questionService;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/addQuestion")
    public DataResult<Integer> addQuestion(@RequestBody CreateQuestionDto createQuestionDto){
        return questionService.addQuestion(createQuestionDto);
    }

    @PostMapping("/deleteQuestion")
    public Result deleteQuestion(@RequestParam int questionId){
        return questionService.deleteQuestion(questionId);
    }

    @PostMapping("/updateQuestion")
    public Result updateQuestion(@RequestBody GetQuestionDto getQuestionDto){
        return questionService.updateQuestion(getQuestionDto);
    }

    @GetMapping("/getQuestionById")
    public DataResult<GetQuestionDto> getQuestionById(@RequestParam int questionId){
        return questionService.getQuestionById(questionId);
    }

    @GetMapping("/getQuestionsByGameId")
    public DataResult<List<GetQuestionDto>> getQuestionsByGameId(@RequestParam String gameId){
        return questionService.getQuestionsByGameId(gameId);
    }

    @GetMapping("/getQuestionEntityById")
    public DataResult<Question> getQuestionEntityById(@RequestParam int questionId){
        return questionService.getQuestionEntityById(questionId);
    }

    @GetMapping("/getQuestionsByHost")
    public DataResult<List<GetQuestionForHostDto>> getQuestionsByHost(){
        return questionService.getQuestionsByHost();
    }

    @PostMapping("/broadcastQuestion")
    public Result broadcastQuestion(@RequestParam int questionId) {
        var questionDto = questionService.getQuestionById(questionId).getData();

        if (questionDto == null) {
            return new SuccessResult("Question not found.");
        }

        // Send the question to the /topic/question channel
        messagingTemplate.convertAndSend("/topic/question", questionDto);

        return new SuccessResult("Question broadcasted to players.");
    }



}