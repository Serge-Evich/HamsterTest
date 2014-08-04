package hamster.netty.http.server.handlers;

import hamster.netty.http.server.stat.RequestStat;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Arrays;

/**
 * Created by Димон on 03.08.2014.
 */
public class HttpRedirectHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, HttpRequest httpRequest) throws Exception{

        String[] uriSplit = httpRequest.getUri().split("url=");

        if(uriSplit.length == 2 && uriSplit[0].contains("/redirect?")) {
            sendRedirect(context, uriSplit[1]);
            RequestStat.addRequestEntity(httpRequest.headers().get("Host"));
        } else {
            context.fireChannelRead(httpRequest);
        }


    }

    private static void sendRedirect(ChannelHandlerContext context, String url) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, url);
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    public static void main(String[] args) {
        String str = "/redirect?url=<url>";
        for(String s : str.split("url="))
            System.out.println(s);
    }

}
