package MykesTool;

import arc.Events;
import arc.graphics.Color;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.NetClient;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.net.Administration.*;
import mindustry.net.Net;
import mindustry.net.NetConnection;
import mindustry.world.blocks.storage.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONObject;

public class MTDBotPlugin extends Plugin{


    private static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    private String m_strLogPrefix = "[MykesTool:ChatBot]";
    private String m_strBotName = "myke";
    private Player m_playerNew;
    //called when game initializes
    @Override
    public void init() {


        //add a chat filter that changes the contents of all messages
        //in this case, all instances of "heck" are censored
        Vars.netServer.admins.addChatFilter((player, text) -> {


            if(m_playerNew == null ) {
                Vars.netServer.admins.updatePlayerJoined("456", "1.2.3.4", m_strBotName);
                m_playerNew = Player.create();
                m_playerNew.name = m_strBotName;
                m_playerNew.admin = true;
                m_playerNew.id = 123;
                m_playerNew.id(123);

                m_playerNew.locale = "en";
                //m_playerNew.color.set(Color.yellow);
                //m_playerNew.added = true;
                m_playerNew.set(100,100);
                Unit unitNew = UnitTypes.gamma.spawn(player.team(), m_playerNew.x, m_playerNew.y);
                unitNew.spawnedByCore = true;
                unitNew.dead = false;
                unitNew.move(100,100);
                m_playerNew.unit(unitNew);
                m_playerNew.unit().move(100,100);
                m_playerNew.team(player.team());
                Groups.player.add(m_playerNew);

                //writeBuffer.reset();
                //player.write(outputBuffer);
                //player.team(assignTeam(player));

                //Vars.netServer.sendWorldData(player);
                Events.fire(new PlayerConnect(player));
            }

            m_playerNew.set(100,100);
            m_playerNew.unit().move(100,100);
            m_playerNew.unit().dead = false;
            //m_playerNew.draw();
            player.sendMessage("test", m_playerNew);
            player.sendMessage("dead:"+m_playerNew.dead(), m_playerNew);
            player.sendMessage("pos:"+m_playerNew.x+m_playerNew.y, m_playerNew);


/*
            int nBotNamePos = -1;
            String strCallName = "@" + m_strBotName;
            String strMsgPrefix = "[red][[[yellow]"+m_strBotName+"的小仆ff[red]][white]";
            nBotNamePos = text.indexOf(strCallName);
            //Log.info(text);
            //Log.info(nBotNamePos);
            if (nBotNamePos == 0) {
                String strAsk = text.substring(strCallName.length());
                String strEncodedAsk = URLEncoder.encode(strAsk, Charset.forName("utf-8"));
                String strURL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + strEncodedAsk;
                String strReply = doGet(strURL);
                //Log.info(m_strLogPrefix+strAsk);
                //Log.info(m_strLogPrefix+strEncodedAsk);

                Log.info(strReply);
                //String json = "{\"2\":\"efg\",\"1\":\"abc\"}";
                //JSONObject json_test = JSONObject.fromObject(strReply);
                final JSONObject jsonResult = new JSONObject(strReply);

                //Log.info(m_strLogPrefix+jsonResult.getInt("result"));
                //Log.info(m_strLogPrefix+jsonResult.getString("content"));
                if (0 == jsonResult.getInt("result")) {
                    //Groups.player.find();
                    String strContent = jsonResult.getString("content");
                    String strFormattedContent = strContent.replace("{br}", "\n");
                    //player.sendMessage(strMsgPrefix + strFormattedContent);
                    text = text + "\n" + strMsgPrefix + strFormattedContent;
                } else {
                    Log.info(m_strLogPrefix + " error getting message.");
                }
            }
            //player.sendMessage("try to do something for v008");
            */
            return text;
        });
    }



}