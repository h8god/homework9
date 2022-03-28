package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import guru.qa.domain.Teacher;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;


public class SelenideFilesTest {
    ClassLoader classLoader = getClass().getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void parseZipFileTest() throws Exception {
        ZipFile zf = new ZipFile("src/test/resources/homework.zip");

        ZipEntry csvEntry = zf.getEntry("username.csv");
        try (InputStream is = zf.getInputStream(csvEntry);
             CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(1)).contains("booker12;9012;Rachel;Booker");
        }

        ZipEntry pdfEntry = zf.getEntry("pdf-sample.pdf");
        try (InputStream is = zf.getInputStream(pdfEntry)) {
            PDF pdf = new PDF(is);
            assertThat(pdf.text).contains("Anyone, anywhere can open a PDF file");
        }

        ZipEntry xlsEntry = zf.getEntry("file_example_XLS_50.xls");
        try (InputStream is = zf.getInputStream(xlsEntry)) {
            XLS xls = new XLS(is);
            assertThat(xls.excel.getSheetAt(0)
                    .getRow(15)
                    .getCell(2)
                    .getStringCellValue())
                    .contains("Hazelton");
        }
    }

    @Test
    void jsonTest() throws Exception {
        Teacher teacher = objectMapper.readValue(classLoader.getResourceAsStream("files/test.json"), Teacher.class);
        assertThat(teacher.name).isEqualTo("Dmitrii");
        assertThat(teacher.address.house).isEqualTo(1);

    }
}

