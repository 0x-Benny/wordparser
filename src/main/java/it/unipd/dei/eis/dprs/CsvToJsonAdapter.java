package it.unipd.dei.eis.dprs;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.*;

public class CsvToJsonAdapter extends StdDeserializer<BasicArticle>
{
    public CsvToJsonAdapter()
    {
        this(null);
    }
    public CsvToJsonAdapter(Class<?> vc)
    {
        super(vc);
    }
    @Override
    public BasicArticle deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        JsonNode node = jp.getCodec().readTree(jp);
        String title = node.get("Title").asText();
        String body = node.get("Body").asText();
        String source = node.get("Source Set").asText();
        return new BasicArticle(title, body, source);
    }
    public static void csvToJson(File csvFile, File jsonFile) throws IOException
    {
        CsvSchema csv = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        try
        {
            MappingIterator<Map<String, String>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(csvFile);
            new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writeValue(jsonFile, mappingIterator.readAll());
        }
        catch (IOException e)
        {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }
    public static ArrayList<BasicArticle> articlesToObject(File json) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BasicArticle.class, new CsvToJsonAdapter());
        mapper.registerModule(module);
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, BasicArticle.class));
    }
}