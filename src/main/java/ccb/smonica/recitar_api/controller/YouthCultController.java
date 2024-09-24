package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.dto.PostAddRecCountDTO;
import ccb.smonica.recitar_api.dto.YouthCultDTO;
import ccb.smonica.recitar_api.service.YouthCultService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "cults")
@Log4j2
public class YouthCultController {
    private YouthCultService service;

    @GetMapping
    @Cacheable("cults")
    public ResponseEntity<List<YouthCultDTO>> listCults(@RequestParam String year, @RequestParam String month,
                                                        @RequestParam(required = false) String day) {
        log.info("Request received in listCults with filters: year={}, month={}, day={}", year, month, day);
        return ResponseEntity.ok(service.findCultsByFilterOrThrowAInvalidRequestException(year, month));
    }

    @PostMapping()
    public ResponseEntity<YouthCultDTO> addCult(@RequestBody PostAddRecCountDTO dto) {
        log.info("Request received in addCult with filters: {}", dto);
        YouthCultDTO response = this.service.addNewRecitativoCount(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<YouthCultDTO> updateCult(@RequestBody PostAddRecCountDTO dto) {
        log.info("Request received in updateCult with filters: {}", dto);
        YouthCultDTO response = this.service.updateRecitativoCount(dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<YouthCultDTO> deleteCult(@RequestParam Long id) {
        log.info("Request received in deleteCult with filters: id -> {}", id);
        this.service.deleteRecitativoCount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/csv")
    public ResponseEntity<YouthCultDTO> csv(@RequestParam("file") MultipartFile file) {
        log.info("Request received in csv: {}", file.getName());
        this.service.registerCountsByCsv(file);
        return ResponseEntity.ok(null);
    }
}
