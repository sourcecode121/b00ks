package com.example.b00ks.model.findBook;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Anand on 09/12/2016.
 */

@Root(name = "search", strict = false)
@Default(DefaultType.PROPERTY)
public class Search {
    private String resultsStart;
    private String resultsEnd;
    private String totalResults;
    private List<Work> results;

    @Element(name = "results-start")
    public String getResultsStart() {
        return resultsStart;
    }

    @Element(name = "results-start")
    public void setResultsStart(String resultsStart) {
        this.resultsStart = resultsStart;
    }

    @Element(name = "results-end")
    public String getResultsEnd() {
        return resultsEnd;
    }

    @Element(name = "results-end")
    public void setResultsEnd(String resultsEnd) {
        this.resultsEnd = resultsEnd;
    }

    @Element(name = "total-results")
    public String getTotalResults() {
        return totalResults;
    }

    @Element(name = "total-results")
    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    @ElementList(name = "results")
    public List<Work> getResults() {
        return results;
    }

    @ElementList(name = "results")
    public void setResults(List<Work> results) {
        this.results = results;
    }
}
