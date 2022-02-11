package pers.machi.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.machi.common.SingletonBeanUtil;
import pers.machi.urimapper.FlowMapper;
import pers.machi.urimapper.UriMapperManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class CustomHttpServerHandler extends SimpleChannelInboundHandler {
    private final Logger logger = LogManager.getLogger(CustomHttpServerHandler.class);
    private HttpRequest request;

    StringBuilder uri = new StringBuilder();
    Map<String, List<String>> params = new HashMap<>();
    StringBuilder content = new StringBuilder();
    LastHttpContent trailer;


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws InvocationTargetException, IllegalAccessException {
        if (msg instanceof HttpRequest) {
            uri.setLength(0);
            params.clear();
            content.setLength(0);
            trailer = null;

            HttpRequest request = this.request = (HttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(request)) {
                writeQuickResponse(ctx, CONTINUE);
            }
            RequestUtils.extractUri(request, uri);
            RequestUtils.extractParams(request, params);
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            RequestUtils.extractBody(httpContent, content);
            if (msg instanceof LastHttpContent) {
                trailer = (LastHttpContent) msg;

                // invoke method by uri mapper
                Method method = UriMapperManagement.getInstance().uri2Method.get(uri.toString());
                if (method != null) {
                    Object invoker = SingletonBeanUtil.getInstance().getBean(method.getDeclaringClass());
                    method.invoke(
                            invoker, params, content.toString());
                    writeResponse(ctx, trailer, uri + "\r\n" + params + "\r\n" + content);

                } else
                    writeQuickResponse(ctx, NOT_FOUND);
            }
        }


    }

    private void writeQuickResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.EMPTY_BUFFER);
        ctx.write(response);
    }

    private void writeResponse(ChannelHandlerContext ctx, LastHttpContent trailer,
                               String responseData) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1,
                ((HttpObject) trailer).decoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(responseData, CharsetUtil.UTF_8));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
                    httpResponse.content().readableBytes());
            httpResponse.headers().set(HttpHeaderNames.CONNECTION,
                    HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(httpResponse);

        if (!keepAlive) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
