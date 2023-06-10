package it.unipd.dei.eis.dprs.tools;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.eis.dprs.BasicArticle;
import java.io.File;
import java.io.IOException;

public class Serializer
{
	public static void serialize(BasicArticle[] articles)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);

			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

			writer.writeValue(new File("./assets/articlesDB.json"), articles);
		}
		catch (IOException e)
		{
			System.err.println("++ERROR. Could not create database. More details:");
			e.printStackTrace();
		}
	}
}
