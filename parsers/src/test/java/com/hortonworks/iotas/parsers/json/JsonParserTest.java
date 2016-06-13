package com.hortonworks.iotas.parsers.json;

import com.hortonworks.iotas.common.Schema;
import com.hortonworks.iotas.exception.ParserException;
import com.hortonworks.iotas.parser.Parser;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonParserTest {

    Parser parser;

    private String loadJson(String fileName) throws IOException {
        InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return IOUtils.toString(jsonStream);
    }

    @Before
    public void setUp() throws Exception {
        parser = new JsonParser();
    }

    @Test
    public void testParseSimple() throws Exception {
        String json = loadJson("simple.json");
        Map<String, Object> res = parser.parse(json);
        assertEquals("value1", res.get("field1"));
        assertEquals("value2", res.get("field2"));
    }

    @Test
    public void testParseSimpleBytes() throws Exception {
        String json = loadJson("simple.json");
        Map<String, Object> res = parser.parse(json.getBytes());
        assertEquals("value1", res.get("field1"));
        assertEquals("value2", res.get("field2"));
    }


    @Test
    public void testParseTypes() throws Exception {
        String json = loadJson("types.json");
        Map<String, Object> res = parser.parse(json);
        assertEquals(100, res.get("intfield"));
        assertEquals(true, res.get("booleanfield"));
        assertEquals("hello", res.get("stringfield"));
        assertEquals(null, res.get("nullfield"));
    }

    @Test(expected = ParserException.class)
    public void testInvalidJson() throws Exception {
        String json = loadJson("invalid.json");
        Map<String, Object> res = parser.parse(json);
    }

    @Test
    public void testParseNested() throws Exception {
        String json = loadJson("nested.json");
        Map<String, Object> res = parser.parse(json);
        assertEquals("iot", res.get("str"));
        assertEquals(Arrays.asList("core", "parser"), res.get("list"));

        Map<String, String> object = new LinkedHashMap<String, String>();
        object.put("f1", "v1");
        object.put("f2", "v2");
        assertEquals(object, res.get("object"));

        Map<String, Integer> obj1 = new LinkedHashMap<String, Integer>();
        Map<String, Integer> obj2 = new LinkedHashMap<String, Integer>();
        obj1.put("str", 1);
        obj2.put("str", 2);
        assertEquals(Arrays.asList(obj1, obj2), res.get("objectarray"));
    }

}
