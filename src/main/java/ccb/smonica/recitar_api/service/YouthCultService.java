package ccb.smonica.recitar_api.service;

import ccb.smonica.recitar_api.dto.PostAddRecCountDTO;
import ccb.smonica.recitar_api.dto.RecitativosDTO;
import ccb.smonica.recitar_api.dto.YouthCultDTO;
import ccb.smonica.recitar_api.entities.RecitativosCount;
import ccb.smonica.recitar_api.entities.YouthCult;
import ccb.smonica.recitar_api.exception.CultsNotFoundException;
import ccb.smonica.recitar_api.repository.RecitativosCountRepository;
import ccb.smonica.recitar_api.repository.YouthCultRepository;
import ccb.smonica.recitar_api.util.CsvUtils;
import ccb.smonica.recitar_api.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Log4j2
public class YouthCultService {
    private YouthCultRepository repository;
    private RecitativosCountRepository recitativosCountRepository;

    public List<YouthCultDTO> findCultsByFilterOrThrowAInvalidRequestException(String year, String month) {
        List<YouthCult> cults = repository.findByYearAndMonth(year, DateUtil.normalizeDayOrMonth(month));

        if (cults.isEmpty()) {
            throw new CultsNotFoundException("Os cultos solicitados nao foram encontrados");
        }

        List<YouthCultDTO> dtos = cults.stream().map(c -> {
            LocalDate date = LocalDate.of(Integer.parseInt(c.getYear()), Integer.parseInt(c.getMonth()),
                    Integer.parseInt(c.getDay()));

            RecitativosCount count = recitativosCountRepository.findByYouthCult(c);
            int total = count.getBoys() + count.getYoungBoys() + count.getGirls() + count.getYoungGirls() + count.getIndividuals();
            RecitativosDTO recDto = RecitativosDTO.builder()
                    .girls(count.getGirls())
                    .youthGirls(count.getYoungGirls())
                    .boys(count.getBoys())
                    .youthBoys(count.getYoungBoys())
                    .individuals(count.getIndividuals())
                    .total(total)
                    .build();

            return YouthCultDTO.builder().date(date).recitativos(recDto).build();
        }).toList();

        return dtos;
    }

    public YouthCultDTO addNewRecitativoCount(PostAddRecCountDTO dto) {
        String day = DateUtil.normalizeDayOrMonth(String.valueOf(dto.cultDate().getDayOfMonth()));
        String month = DateUtil.normalizeDayOrMonth(String.valueOf(dto.cultDate().getMonthValue()));
        String year = String.valueOf(dto.cultDate().getYear());

        YouthCult cult = this.repository.findByYearAndMonthAndDay(year, month, day);

        if (cult == null) {
            log.debug("Cult doesnt exists, creating it.");

            cult = YouthCult.builder()
                    .day(day)
                    .month(month)
                    .year(year)
                    .build();
            this.repository.save(cult);
        }

        RecitativosCount entity = RecitativosCount.builder()
                .boys(dto.boys())
                .youngBoys(dto.youthBoys())
                .girls(dto.girls())
                .youngGirls(dto.youthGirls())
                .individuals(dto.individuals())
                .youthCult(cult)
                .build();

        RecitativosCount created = this.recitativosCountRepository.save(entity);
        int total = dto.boys() + dto.youthBoys() + dto.girls() + dto.youthGirls() + dto.individuals();

        return YouthCultDTO.builder()
                .recitativos(
                        RecitativosDTO.builder()
                                .boys(created.getBoys())
                                .youthBoys(created.getYoungBoys())
                                .girls(created.getGirls())
                                .youthGirls(created.getYoungGirls())
                                .individuals(created.getIndividuals())
                                .total(total)
                                .build()
                )
                .date(dto.cultDate())
                .build();
    }

    public YouthCultDTO updateRecitativoCount(PostAddRecCountDTO dto) {
        String day = DateUtil.normalizeDayOrMonth(String.valueOf(dto.cultDate().getDayOfMonth()));
        String month = DateUtil.normalizeDayOrMonth(String.valueOf(dto.cultDate().getMonthValue()));
        String year = String.valueOf(dto.cultDate().getYear());

        YouthCult cult = this.repository.findByYearAndMonthAndDay(year, month, day);

        if (cult == null) {
            throw new CultsNotFoundException("Nao foi encontardo culto registrado neste dia");
        }

        RecitativosCount count = this.recitativosCountRepository.findByYouthCult(cult);

        if (count != null) {
            count.setBoys(dto.boys());
            count.setYoungBoys(dto.youthBoys());
            count.setGirls(dto.girls());
            count.setYoungGirls(dto.youthGirls());
            count.setIndividuals(dto.individuals());
            this.recitativosCountRepository.save(count);
        }

        int total = dto.boys() + dto.youthBoys() + dto.girls() + dto.youthGirls() + dto.individuals();

        return YouthCultDTO.builder()
                .date(dto.cultDate())
                .recitativos(
                        RecitativosDTO.builder()
                                .boys(dto.boys())
                                .youthBoys(dto.youthBoys())
                                .girls(dto.girls())
                                .youthGirls(dto.youthGirls())
                                .individuals(dto.individuals())
                                .total(total)
                                .build()
                )
                .build();
    }

    public void deleteRecitativoCount(Long id) {
        try {
            RecitativosCount entity = this.recitativosCountRepository.findById(id).get();
            this.recitativosCountRepository.delete(entity);
        } catch (NoSuchElementException e) {
            log.debug("NoSuchElement Exception was raised");
            throw new CultsNotFoundException("Count does not exist");
        }
    }

    public void registerCountsByCsv(MultipartFile file) {
        try {
            List<PostAddRecCountDTO> dtos =  CsvUtils.read(PostAddRecCountDTO.class, file.getInputStream());
            dtos.forEach(this::addNewRecitativoCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
