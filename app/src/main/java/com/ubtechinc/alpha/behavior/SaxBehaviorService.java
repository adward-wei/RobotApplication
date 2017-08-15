package com.ubtechinc.alpha.behavior;

import com.ubtechinc.alpha.sdk.led.Led;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedEffect;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/10
 * @modifier:
 * @modify_time:
 */

public class SaxBehaviorService {

    public BehaviorSet getBehavior(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader parser = factory.newSAXParser().getXMLReader();
        BehaviorHandler handler = new BehaviorHandler();
        parser.setContentHandler(handler);
        parser.parse(new InputSource(in));
        in.close();
        return handler.getRoot();
    }

    private class BehaviorHandler extends DefaultHandler {
        private BehaviorSet root;
        private BehaviorSet curSet;
        private BehaviorSet parentSet;
        private Behavior curBehavior;

        public BehaviorSet getRoot() {
            return root;
        }

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (Names.SET.equals(localName)){
                curBehavior = new BehaviorSet();
                if (root == null) {
                    root = (BehaviorSet) curBehavior;
                    curSet = root;
                }else {
                    parentSet = curSet;
                    curSet = (BehaviorSet) curBehavior;
                }
                handleSetAttributes(curSet, attributes);
                if (parentSet != null) {
                    parentSet.addChildBehavior(curSet);
                    curSet.setParent(parentSet);
                }
            }else if (Names.ACTION.equals(localName)){
                curBehavior = new ActionBehavior();
                handleActionAttributes((ActionBehavior) curBehavior, attributes);
            }else if (Names.LED.equals(localName)){
                curBehavior = new LedBehavior();
                handleLedAttributes((LedBehavior) curBehavior, attributes);
            }else if (Names.TTS.equals(localName)){
                curBehavior = new TtsBehavior();
                handleTtsAttributes((TtsBehavior) curBehavior, attributes);
            }else if (Names.MUSIC.equals(localName)){
                curBehavior = new MusicBehavior();
                handleMusicAttributes((MusicBehavior) curBehavior, attributes);
            }else {
                throw new SAXException("no supported element;");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (Names.SET.equals(localName)){
                if (parentSet != null) {
                    curSet = parentSet;
                    parentSet = curSet.getParent();
                }
            }else if (Names.ACTION.equals(localName)){
                curSet.addChildBehavior(curBehavior);
            }else if (Names.LED.equals(localName)){
                curSet.addChildBehavior(curBehavior);
            }else if (Names.TTS.equals(localName)){
                curSet.addChildBehavior(curBehavior);
            }else if (Names.MUSIC.equals(localName)){
                curSet.addChildBehavior(curBehavior);
            }else {
                throw new SAXException("no supported element");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
        }
    }

    private void handleSetAttributes(Behavior curSet, Attributes attributes) throws SAXException {
        for (int i = 0 ; i < attributes.getLength(); i++){
            if (attributes.getLocalName(i).equals(Names.ORDER)){
                try {
                    curSet.setOrder(limitOrder(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [id] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.DELAY)){
                try {
                    curSet.setDelayTime(limitDelay(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [delay] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.REPEAT)){
                try {
                    curSet.setRepeat(limitRepeat(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [repeat] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }
            //else {
            //    throw new SAXException("invalid set attribute: "+ attributes.getLocalName(i));
            //}
        }
    }

    private void handleActionAttributes(ActionBehavior curBehavior, Attributes attributes) throws SAXException {
        for (int i = 0 ; i < attributes.getLength(); i++){
            if (attributes.getLocalName(i).equals(Names.ORDER)){
                try {
                    curBehavior.setOrder(limitOrder(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [id] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.DELAY)){
                try {
                    curBehavior.setDelayTime(limitDelay(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [delay] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.REPEAT)){
                try {
                    curBehavior.setRepeat(limitRepeat(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [repeat] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.ACTION)){
                curBehavior.setAction(attributes.getValue(i));
            }else if(attributes.getLocalName(i).equals(Names.SPEED)){
                try {
                    curBehavior.setSpeed(limitSpeed(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [speed] value [" + attributes.getValue(i) +"], need a integer!");
                }
            }
            //else {
            //    throw new SAXException("invalid action attribute: "+ attributes.getLocalName(i));
            //}
        }
    }

    private void handleTtsAttributes(TtsBehavior curBehavior, Attributes attributes) throws SAXException {
        for (int i = 0 ; i < attributes.getLength(); i++){
            if (attributes.getLocalName(i).equals(Names.ORDER)){
                try {
                    curBehavior.setOrder(limitOrder(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [id] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.DELAY)){
                try {
                    curBehavior.setDelayTime(limitDelay(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [delay] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.REPEAT)){
                try {
                    curBehavior.setRepeat(limitRepeat(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [repeat] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.TEXT)){
                curBehavior.setText(attributes.getValue(i));
            }else if (attributes.getLocalName(i).equals(Names.VOICER)){
                curBehavior.setVoicer(attributes.getValue(i));
            }else if (attributes.getLocalName(i).equals(Names.SPEED)){
                try {
                    curBehavior.setSpeed(limitSpeed(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [speed] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.MOOD)){
                curBehavior.setMood(attributes.getValue(i));
            }else if(attributes.getLocalName(i).equals(Names.VOLUME)){
                try {
                    curBehavior.setSpeed(Integer.valueOf(attributes.getValue(i)));
                }catch (Exception e){
                    throw new SAXException("invalid [volume] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }
            //else {
            //    throw new SAXException("invalid tts attribute: "+ attributes.getLocalName(i));
            //}
        }
    }

    private void handleLedAttributes(LedBehavior curBehavior, Attributes attributes) throws SAXException {
        for (int i = 0 ; i < attributes.getLength(); i++){
            if (attributes.getLocalName(i).equals(Names.ORDER)){
                try {
                    curBehavior.setOrder(limitOrder(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [id] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.DELAY)){
                try {
                    curBehavior.setDelayTime(limitDelay(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [delay] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.REPEAT)){
                try {
                    curBehavior.setRepeat(limitRepeat(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [repeat] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.POSITION)){
                try {
                    curBehavior.setLed(Led.valueOf(attributes.getValue(i).toUpperCase()));
                }catch(Exception e) {
                    throw new SAXException("invalid [position] value [" + attributes.getValue(i) +"], need be one of [ear, eye, head, mouth]!");
                }
            }else if(attributes.getLocalName(i).equals(Names.NUMBER)){
                if (isHex2BitStr(attributes.getValue(i))){
                    curBehavior.setNumber(hex2int(attributes.getValue(i)));
                }else {
                    throw new SAXException("invalid [number] value [" + attributes.getValue(i) +"], need a 8-bit hex string!");
                }
            }else if (attributes.getLocalName(i).equals(Names.ON_TIME)){
                try {
                    curBehavior.setOnTime(limitOntime(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [ontime] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.RUN_TIME)){
                try {
                    curBehavior.setRunTime(limitRuntime(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [runtime] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.COLOR)){
                try {
                    curBehavior.setColor(LedColor.valueOf(attributes.getValue(i).toUpperCase()));
                }catch (Exception e){
                    throw new SAXException("invalid [color] value [" + attributes.getValue(i) +"], need a value in [1 - 8]");
                }
            }else if(attributes.getLocalName(i).equals(Names.SECOND_COLOR)){
                try {
                    curBehavior.setSecondColor(LedColor.valueOf(attributes.getValue(i).toUpperCase()));
                }catch (Exception e){
                    throw new SAXException("invalid [secondColor] value [" + attributes.getValue(i) +"], need a value in [1 - 8]");
                }
            }else if (attributes.getLocalName(i).equals(Names.BRIGHT)){
                try {
                    int value = Math.max(0,Math.min(9,Integer.valueOf(attributes.getValue(i))));
                    curBehavior.setBright(LedBright.valueOf(value));
                }catch (Exception e){
                    throw new SAXException("invalid [bright] value [" + attributes.getValue(i) +"], need a value in [0 - 9]!");
                }
            }else if (attributes.getLocalName(i).equals(Names.EFFECT)){
                try {
                    curBehavior.setEffect(LedEffect.valueOf(attributes.getValue(i).toUpperCase()));
                }catch (Exception e){
                    throw new SAXException("invalid [effect] value [" + attributes.getValue(i) +"], need be one of [blink, breath, horse]");
                }
            }
            //else {
            //    throw new SAXException("invalid led attribute:"+ attributes.getLocalName(i));
            //}
        }
    }

    private void handleMusicAttributes(MusicBehavior curBehavior, Attributes attributes) throws SAXException {
        for (int i = 0 ; i < attributes.getLength(); i++){
            if (attributes.getLocalName(i).equals(Names.ORDER)){
                try {
                    curBehavior.setOrder(limitOrder(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [id] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.DELAY)){
                try {
                    curBehavior.setDelayTime(limitDelay(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [delay] value [" + attributes.getValue(i) +"], need an integer!");
                }
            }else if (attributes.getLocalName(i).equals(Names.REPEAT)){
                try {
                    curBehavior.setRepeat(limitRepeat(Integer.valueOf(attributes.getValue(i))));
                }catch (Exception e){
                    throw new SAXException("invalid [repeat] value [" + attributes.getValue(i) +"], need an integer!");
                }
            } else if (attributes.getLocalName(i).equals(Names.URI)){
                if (attributes.getValue(i).startsWith(Names.HTTP_PREFFIX)
                        || attributes.getValue(i).startsWith(Names.FILE_PREFFIX)){
                    curBehavior.setUrl(attributes.getValue(i));
                }else {
                    throw new SAXException("invalid [uri] value [" + attributes.getValue(i) +"], need a start with "+Names.HTTP_PREFFIX
                            + " or " + Names.FILE_PREFFIX + " string!");
                }
            }
            //else {
            //    throw new SAXException("invalid music attribute:"+attributes.getLocalName(i));
            //}
        }
    }

    public static boolean isHex8BitStr(String origin) {
        Pattern pattern = Pattern.compile("^[A-Fa-f0-9]{8}");
        Matcher matcher = pattern.matcher(origin);
        return matcher.matches();
    }

    public static boolean isHex2BitStr(String origin){
        Pattern pattern = Pattern.compile("^[A-Fa-f0-9]{2}");
        Matcher matcher = pattern.matcher(origin);
        return matcher.matches();
    }

    public static int hex2int(String hexstr){
        int sum = 0 ;
        final int length = hexstr.length();
        assert length <=8 && length > 0;
        hexstr = hexstr.toUpperCase();
        int pow = 0;
        for (int i =0 ; i < length; i++){
            switch (hexstr.charAt(i)){
                case '0':
                    pow = 0;
                    break;
                case '1':
                    pow = 1;
                    break;
                case '2':
                    pow = 2;
                    break;
                case '3':
                    pow = 3;
                    break;
                case '4':
                    pow = 4;
                    break;
                case '5':
                    pow = 5;
                    break;
                case '6':
                    pow = 6;
                    break;
                case '7':
                    pow = 7;
                    break;
                case '8':
                    pow = 8;
                    break;
                case '9':
                    pow = 9;
                    break;
                case 'A':
                    pow = 10;
                    break;
                case 'B':
                    pow = 11;
                    break;
                case 'C':
                    pow = 12;
                    break;
                case 'D':
                    pow = 13;
                    break;
                case 'E':
                    pow = 14;
                    break;
                case 'F':
                    pow = 15;
                    break;
                default:
                    pow = 0;
                    break;
            }
            sum += Math.pow(pow,length- i - 1) * 16;
        }
        return sum;
    }

    public static boolean isSupportBright(int bright){
        return bright >= LedBright.ZERO.value && bright <= LedBright.NINE.value;
    }

    private static int limitRepeat(int repeat){
        return Math.min(100, Math.max(0, repeat));
    }

    private static int limitDelay(int delay){
        return Math.min(100000000, Math.max(0, delay));
    }

    private static int limitOrder(int order){
        return Math.min(100, Math.max(0, order));
    }

    private static int limitRuntime(int runtime){
        return Math.min(60000, Math.max(100, runtime));
    }

    private static int limitOntime(int ontime){
        return Math.min(30000, Math.max(50, ontime));
    }

    private static int limitSpeed(int speed){
        return Math.min(100, Math.max(1, speed));
    }
}
