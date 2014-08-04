package hamster.netty.http.server.handlers;


import hamster.netty.http.server.stat.RequestStat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.TimeUnit;


/**
 * Created by Димон on 02.08.2014.
 */
public class HelloWorldHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    public void channelRead0(final ChannelHandlerContext context, HttpRequest httpRequest) throws Exception {
        System.out.println(httpRequest);

        if (httpRequest.getUri().equals("/hello")) {
            context.executor().schedule(new HelloWorldResponse(context), 10, TimeUnit.SECONDS);
            RequestStat.addRequestEntity(httpRequest.headers().get("Host"));
        } else {
            context.fireChannelRead(httpRequest);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
