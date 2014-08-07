package hamster.netty.http.server.handlers;


import hamster.netty.http.server.stat.RequestStat;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;


/**
 * Created by Димон on 02.08.2014.
 */
public class HelloWorldHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private static final String HELLO = "Hello World!";

    @Override
    public void channelRead0(final ChannelHandlerContext context, HttpRequest httpRequest) throws Exception {
        System.out.println(httpRequest);

        if (httpRequest.getUri().equals("/hello")) {
            RequestStat.addIpEntity(httpRequest.headers().get("Host"), httpRequest.getUri());
            context.executor().schedule(new Runnable() {
                public void run() {
                    try {
                        FullHttpResponse response =
                                new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(HELLO.getBytes("UTF-8")));
                        response.headers().set(CONTENT_TYPE, "text/plain");
                        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 10, TimeUnit.SECONDS);

        } else {
            context.fireChannelRead(httpRequest);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
