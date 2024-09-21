package ccb.smonica.recitar_api.service;

import ccb.smonica.recitar_api.dto.RecitativosDTO;
import ccb.smonica.recitar_api.dto.YouthCultDTO;
import ccb.smonica.recitar_api.exception.ReportFileNotFoundException;
import com.spire.doc.Document;
import com.spire.doc.ToPdfParameterList;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportService {
    @Value("${spring.reports.model-path}")
    private String reportModelPath;

    @Value("${spring.reports.model-filename}")
    private String reportModelFilename;

    private final YouthCultService youthCultService;

    public Map<String, String> getKeyValueDocxReplacements(List<YouthCultDTO> dtos) {
        YouthCultDTO dayWithMostRec = null;
        YouthCultDTO dayWithLeastRec = null;
        Map<String, String> replacements = new HashMap<>();
        int sumGirls = 0;
        int sumBoys = 0;
        int sumYouthBoys = 0;
        int sumYouthGirls = 0;
        int sumIndividuals = 0;
        int sumTotal = 0;
        int order = 1;

        replacements.put("%MES%", dtos.get(0).getDate().getMonth().getDisplayName(
                TextStyle.FULL, Locale.of("pt", "BR")).toUpperCase());
        replacements.put("%ANO%", String.valueOf(dtos.get(0).getDate().getYear()));

        for (YouthCultDTO day : dtos) {
            RecitativosDTO rec = day.getRecitativos();
            replacements.put(String.format("%%D%d%%", order), day.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            replacements.put(String.format("%%MA%d%%", order), String.valueOf(rec.getGirls()));
            replacements.put(String.format("%%YA%d%%", order), String.valueOf(rec.getYouthGirls()));
            replacements.put(String.format("%%MO%d%%", order), String.valueOf(rec.getBoys()));
            replacements.put(String.format("%%YO%d%%", order), String.valueOf(rec.getYouthBoys()));
            replacements.put(String.format("%%I%d%%", order), String.valueOf(rec.getIndividuals()));
            replacements.put(String.format("%%T%d%%", order), String.valueOf(rec.getTotal()));

            if (dayWithMostRec == null || dayWithMostRec.getRecitativos().getTotal() < rec.getTotal()) {
                dayWithMostRec = day;
            }
            if (dayWithLeastRec == null || dayWithMostRec.getRecitativos().getTotal() > rec.getTotal()) {
                dayWithLeastRec = day;
            }

            sumGirls += rec.getGirls();
            sumYouthBoys += rec.getYouthBoys();
            sumBoys += rec.getBoys();
            sumIndividuals += rec.getIndividuals();
            sumYouthGirls += rec.getYouthGirls();
            sumTotal += rec.getTotal();

            order++;
        }

        replacements.put("%D_M%", dayWithMostRec.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        replacements.put("%N_M%", String.valueOf(dayWithMostRec.getRecitativos().getTotal()));
        replacements.put("%D_N%", dayWithLeastRec.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        replacements.put("%N_N%", String.valueOf(dayWithLeastRec.getRecitativos().getTotal()));
        replacements.put("%CMA%", String.format("%.2f", (double) sumGirls / dtos.size()));
        replacements.put("%CYA%", String.format("%.2f", (double) sumYouthGirls / dtos.size()));
        replacements.put("%CMO%", String.format("%.2f", (double) sumBoys / dtos.size()));
        replacements.put("%CYO%", String.format("%.2f", (double) sumYouthBoys / dtos.size()));
        replacements.put("%CIO%", String.format("%.2f", (double) sumIndividuals / dtos.size()));
        replacements.put("%CT%", String.format("%.2f", (double) sumTotal / dtos.size()));

        return replacements;
    }

    public InputStreamResource generateMonthlyReport(String year, String month) {
        List<YouthCultDTO> dtos = this.youthCultService.findCultsByFilterOrThrowAInvalidRequestException(year, month);
        Map<String, String> replacements = getKeyValueDocxReplacements(dtos);
        String path;

        try {
            path = this.createModelCopy(year, month);
            //Files.delete(Path.of(path));
            String pdf = doReplacementsOnDOCX(path, replacements);
            InputStreamResource r = new InputStreamResource(new FileInputStream(pdf));
            return r;
        } catch (IOException | JAXBException | Docx4JException e) {
            log.error("Something went wrong during processing new report");
            throw new RuntimeException(e);
        }
    }

    private String createModelCopy(String year, String month) throws IOException {
        String copyFullPath = reportModelPath + String.format("%s-%s-report.docx", year, month);
        Files.copy(Path.of(reportModelPath + reportModelFilename), Path.of(copyFullPath));

        return copyFullPath;
    }

    public String doReplacementsOnDOCX(String filePath, Map<String, String> replacements)
            throws JAXBException, Docx4JException {

        File doc = new File(filePath);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                .load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage
                .getMainDocumentPart();
        String textNodesXPath = "//w:t";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);

        for (Object obj : textNodes) {
            Text text = (Text) ((JAXBElement) obj).getValue();
            String textValue = text.getValue();

            for (String replacement : replacements.keySet()) {
                if (textValue.contains(replacement)) {
                    textValue = textValue.replace(replacement, replacements.get(replacement));
                }
            }
            text.setValue(textValue);
        }

        wordMLPackage.save(doc);
        String pdfFullPath = doc.getParentFile().getAbsolutePath() + File.separator
                + doc.getName().split("\\.")[0] + ".pdf";
        convertDocxToPdf(doc.getAbsolutePath(), pdfFullPath);
        return pdfFullPath;
    }

    private void convertDocxToPdf(String docxPath, String pdfPath) {
        Document doc = new Document(docxPath);
        ToPdfParameterList parameterList = new ToPdfParameterList();
        doc.saveToFile(pdfPath, parameterList);

        try {
            Files.delete(Paths.get(docxPath));
        } catch (IOException e) {
            throw new ReportFileNotFoundException(e.getMessage(), reportModelFilename);
        }
    }
}
