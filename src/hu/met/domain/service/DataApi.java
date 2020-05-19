package hu.met.domain.service;

import hu.met.domain.model.Report;

import javax.sql.DataSource;
import java.util.List;

public class DataApi {

    private final FileReader fileReader;
    private final DataParser dataParser;

    public DataApi(FileReader fileReader, DataParser dataParser) {
        this.fileReader = fileReader;
        this.dataParser = dataParser;
    }

    public List<Report> getData(String input) {
        return dataParser.parse(fileReader.read(input));
    }
}
