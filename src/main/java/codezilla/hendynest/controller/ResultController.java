package codezilla.hendynest.controller;

import codezilla.hendynest.model.ResponseDto;
import codezilla.hendynest.service.ResultService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/result")
public class ResultController {

    private final ResultService resultService;

    @PostMapping(path = "/{operationId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseDto getResult(
            @PathVariable
            Long operationId,
            @RequestBody @NotEmpty
            MultiValueMap<String,String> map,
            HttpServletRequest request
    ) {
        System.out.println(map);
        return resultService.getResponse(map,request);
    }

}
