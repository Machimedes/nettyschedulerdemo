package pers.machi.server;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestUtils {

    static void extractUri(HttpRequest request, StringBuilder uri) {
        uri.append(request.uri().split("\\?")[0]);
    }

    static void extractParams(HttpRequest request, Map<String, List<String>> params) {
        (new QueryStringDecoder(request.uri())).parameters()
                .forEach(params::put);
    }

    static StringBuilder extractBody(HttpContent httpContent, StringBuilder content) {
        StringBuilder responseData = new StringBuilder();
        ByteBuf _content = httpContent.content();
        if (_content.isReadable()) {
            content.append(_content.toString(CharsetUtil.UTF_8)).append("\r\n");
        }
        return responseData;
    }

    static StringBuilder prepareLastResponse(HttpRequest request, LastHttpContent trailer) {
        StringBuilder responseData = new StringBuilder();
        responseData.append("\r\n");

        if (!trailer.trailingHeaders().isEmpty()) {
            responseData.append("\r\n");
            for (CharSequence name : trailer.trailingHeaders().names()) {
                for (CharSequence value : trailer.trailingHeaders().getAll(name)) {
                    responseData.append(name).append(" = ").append(value).append("\r\n");
                }
            }
            responseData.append("\r\n");
        }
        return responseData;
    }
}
