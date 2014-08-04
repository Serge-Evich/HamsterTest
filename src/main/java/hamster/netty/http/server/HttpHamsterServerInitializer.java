package hamster.netty.http.server;

import hamster.netty.http.server.handlers.HelloWorldHandler;
import hamster.netty.http.server.handlers.HttpRedirectHandler;
import hamster.netty.http.server.handlers.HttpStatusHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * Created by Димон on 02.08.2014.
 */
public class HttpHamsterServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpHamsterServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("codec", new HttpServerCodec());
        p.addLast("hello-handler", new HelloWorldHandler());
        p.addLast("redirect-handler", new HttpRedirectHandler());
        p.addLast("status-handler", new HttpStatusHandler());

    }

}
