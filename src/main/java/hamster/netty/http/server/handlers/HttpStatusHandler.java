package hamster.netty.http.server.handlers;

import hamster.netty.http.server.stat.IpEntity;
import hamster.netty.http.server.stat.RedirectEntity;
import hamster.netty.http.server.stat.RedirectStat;
import hamster.netty.http.server.stat.RequestStat;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Created by Димон on 03.08.2014.
 */
public class HttpStatusHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    public void channelRead0(ChannelHandlerContext context, HttpRequest httpRequest) {
        if (httpRequest.getUri().equals("/status")) {

            FullHttpResponse response =  new DefaultFullHttpResponse(HTTP_1_1, OK,
                            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(getStatusPage(), CharsetUtil.UTF_8)));

            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

            response.headers().set(CONTENT_TYPE, "text/html");

            RequestStat.addIpEntity(httpRequest.headers().get("Host"), httpRequest.getUri());

            context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

        } else {
            context.fireChannelRead(httpRequest);
        }
    }

    private String getStatusPage() {

        StringBuilder stringBuilder = new StringBuilder("<!DOCTYPE html>\n<html>\n<body>");
        stringBuilder.append("Total Request Number: ");
        stringBuilder.append(RequestStat.getAllReqCount());
        stringBuilder.append("<br>");
        stringBuilder.append("Unique Request Number: ");
        stringBuilder.append(RequestStat.getUniReqCount());
        stringBuilder.append("<br>");
        stringBuilder.append("Request Table");
        stringBuilder.append("<table border=\"1\">");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>").append("IP").append("</td>");
        stringBuilder.append("<td>").append("req_count").append("</td>");
        stringBuilder.append("<td>").append("req_last_time").append("</td>");
        stringBuilder.append("<tr>");

        for(Map.Entry<String, IpEntity> requestEntry : RequestStat.getReqEntityMap().entrySet()) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>").append(requestEntry.getKey()).append("</td>");
            stringBuilder.append("<td>").append(requestEntry.getValue().getRequestCount()).append("</td>");
            stringBuilder.append("<td>").append(dateFormat(requestEntry.getValue().getLastTime())).append("</td>");
            stringBuilder.append("<tr>");
        }
        stringBuilder.append("</table>");
        stringBuilder.append("<br>");

        stringBuilder.append("Redirect Table");
        stringBuilder.append("<table border=\"1\">");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>").append("URL").append("</td>");
        stringBuilder.append("<td>").append("url_count").append("</td>");
        stringBuilder.append("<tr>");

        for(Map.Entry<String, RedirectEntity> redirectEntry : RedirectStat.getRedirectMap().entrySet()) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>").append(redirectEntry.getKey()).append("</td>");
            stringBuilder.append("<td>").append(redirectEntry.getValue().getRedirectCount()).append("</td>");
            stringBuilder.append("<tr>");
        }
        stringBuilder.append("</table>");
        stringBuilder.append("<br>");

        stringBuilder.append("Last Connections Table");
        stringBuilder.append("<table border=\"1\">");
        stringBuilder.append("<tr>");
        stringBuilder.append("<td>").append("src_ip").append("</td>");
        stringBuilder.append("<td>").append("uri").append("</td>");
        stringBuilder.append("<td>").append("timestamp").append("</td>");
        stringBuilder.append("<tr>");
        for (Map.Entry<Long, IpEntity> entry : RequestStat.getLastConnections().entrySet()) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>").append(entry.getValue().getIp()).append("</td>");
            stringBuilder.append("<td>").append(entry.getValue().getLastUrl()).append("</td>");
            stringBuilder.append("<td>").append(entry.getValue().getLastTime()).append("</td>");
            stringBuilder.append("<tr>");
        }
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");



        return stringBuilder.toString();
    }
    private static String dateFormat(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy_HH:mm:ssss");
        return simpleDateFormat.format(date);
    }

}
