package cc.trity.sun.sdk;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cc.trity.sun.model.city.City;
import cc.trity.sun.model.city.County;
import cc.trity.sun.model.city.Province;

/**
 * 解析城市city.xml，并返回list集合
 * Created by TryIT on 2016/1/21.
 */
public class PlaceSaxParseHandler extends DefaultHandler {
    private static final String TAG = "PlaceSaxParseHandler";
    private List<Province> provinceList;
    private Province province;
    private City city;
    private County county;

    public static List<Province> getProvicneModel(InputStream in) throws Exception {
        PlaceSaxParseHandler handler = null;
        // 得到SAX解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 创建解析器
        SAXParser parser = factory.newSAXParser();
        XMLReader xmlreader = parser.getXMLReader();
        // 得到输入流
        InputSource is = new InputSource(in);
        // 得到SAX解析实现类
        handler = new PlaceSaxParseHandler();
        xmlreader.setContentHandler(handler);
        // 开始解析
        xmlreader.parse(is);

        return handler.provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        provinceList = new ArrayList<>(34);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("province")) {
            province = new Province();
            province.setId(attributes.getValue("id"));
            province.setPlaceName(attributes.getValue("name"));
        } else if (localName.equals("city")) {
            city = new City();
            city.setId(attributes.getValue("id"));
            city.setPlaceName(attributes.getValue("name"));

        } else if (localName.equals("county")) {
            county = new County();
            county.setId(attributes.getValue("id"));
            county.setPlaceName(attributes.getValue("name"));
            county.setWeaterCode(attributes.getValue("weatherCode"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("province")) {
            provinceList.add(province);
            province = null;
        } else if (localName.equals("city")) {
            province.getCityList().add(city);
            city = null;
        } else if (localName.equals("county")) {
            city.getCountyList().add(county);
            county = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {

    }
}
