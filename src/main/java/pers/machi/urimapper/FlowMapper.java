package pers.machi.urimapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.server.CustomHttpServerHandler;
import pers.machi.urimapper.annotation.UriMapper;
import pers.machi.urimapper.annotation.UriMethod;

import java.util.List;
import java.util.Map;

@UriMapper
public class FlowMapper {
    private final Logger logger = LogManager.getLogger(FlowMapper.class);

    @UriMethod(uri = "/flow/submit")
    public String flowSubmit(Map<String, List<String>> params, String content){
        logger.warn("/flow/submit");





        return "flow submit";
    }
}
