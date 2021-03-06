package pers.machi.urimapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.flow.Flow;
import pers.machi.urimapper.annotation.UriMapper;
import pers.machi.urimapper.annotation.UriMethod;

import java.util.List;
import java.util.Map;

@UriMapper
public class FlowMapper {
    private final Logger logger = LogManager.getLogger(FlowMapper.class);

    // annotate method with its uri
    @UriMethod(uri = "/flow/submit")
    public String flowSubmit(Map<String, List<String>> params, String content){
        Flow.createAndRegisterFlow(content);





        return "flow submit";
    }
}
