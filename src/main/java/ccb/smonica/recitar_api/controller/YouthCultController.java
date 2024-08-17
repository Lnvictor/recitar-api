package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.dto.PostAddRecCountDTO;
import ccb.smonica.recitar_api.dto.YouthCultDTO;
import ccb.smonica.recitar_api.service.YouthCultService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cults")
@AllArgsConstructor
public class YouthCultController {
    private YouthCultService service;

    @GetMapping
    public ResponseEntity<List<YouthCultDTO>> listCults(@RequestParam String year, @RequestParam String month,
                                                        @RequestParam(required = false) String day){
        return ResponseEntity.ok(service.findCultsByFilterOrThrowAInvalidRequestException(year, month));
    }

    @PostMapping()
    public ResponseEntity<YouthCultDTO> addCult(@RequestBody PostAddRecCountDTO dto){
        YouthCultDTO response = this.service.addNewRecitativoCount(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<YouthCultDTO> updateCult(@RequestBody PostAddRecCountDTO dto){
        YouthCultDTO response = this.service.updateRecitativoCount(dto);
        return ResponseEntity.ok(response);
    }
}
