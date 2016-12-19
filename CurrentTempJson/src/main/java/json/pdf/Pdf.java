package json.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import json.current.temp.CityAndTemp;

public class Pdf {

	private static Scanner input = new Scanner(System.in);
	private static Document document = new Document();
	private static Logger logger = Logger.getLogger(Pdf.class);

	public static void inputStream(Properties prop, File file) {

		try (InputStream inputStream = new FileInputStream(file)) {

			prop.load(inputStream);

		} catch (IOException e) {
			logger.error("Unable to load properties file!");
			logger.debug(e, e);
		}
	}

	public static Properties citiesToUrl() {

		Properties properties = new Properties();
		File file = new File("src/main/resources/properties/cities.properties");
		inputStream(properties, file);

		properties.getProperty("cities-id");

		return properties;

	}

	public static Paragraph addValuesToPdf(ArrayList<String> city, ArrayList<String> cuTemp, ArrayList<String> minTe,
			ArrayList<String> maxTe, ArrayList<String> press) throws Exception {

		Paragraph paragraph = new Paragraph();
		Properties properties = new Properties();
		File file = new File("src/main/resources/properties/pdfconfig.properties");
		inputStream(properties, file);

		String fontName = properties.getProperty("font-text");
		int fontSize = Integer.parseInt(properties.getProperty("font-text-size"));
		Font font = FontFactory.getFont(fontName, fontSize);

		paragraph.setFont(font);

		for (int i = 0; i < city.size(); i++) {

			paragraph.add("\n" + city.get(i) + ": Current temperature:" + cuTemp.get(i) + " Minimal temperature:"
					+ minTe.get(i) + " Maximal temperature:" + maxTe.get(i) + " Pressure:" + press.get(i) + "\n");

		}

		document.add(paragraph);

		return paragraph;
	}

	public static Paragraph paragraphTitle() throws Exception {

		Paragraph paragraph = new Paragraph();
		Properties properties = new Properties();
		File file = new File("src/main/resources/properties/pdfconfig.properties");
		inputStream(properties, file);

		String titleText = properties.getProperty("title-text");
		int alignment = Integer.parseInt(properties.getProperty("title-alignment"));
		String titleFont = properties.getProperty("title-font");
		int fontSize = Integer.parseInt(properties.getProperty("font-size-title"));
		Font font = FontFactory.getFont(titleFont, fontSize);

		paragraph.setFont(font);
		paragraph.setAlignment(alignment);
		paragraph.add(titleText);

		document.add(paragraph);

		return paragraph;

	}

	public static String inputFileName() {

		boolean check = true;
		String inName = null;

		while (check) {
			logger.info("Enter the file name:");
			inName = input.nextLine();
			if (inName.isEmpty()) {
				logger.warn("Filename cannot be empty!");
				continue;
			}
			if (inName.matches(".*[\\/:*?><|\"].*")) {
				logger.warn("A filename cannot contain any of the following characters \\ / : * ? \" < > |");
				continue;
			} else {
				inName = "src/main/resources/pdf/" + inName + ".pdf";
				check = false;
			}
		}
		return inName;
	}

	public static void createPdf(String fileName) throws Exception {

		File file = new File(fileName);

		if (file.exists() == true) {
			throw new FileAlreadyExistsException(fileName);
		}
		try (OutputStream outputStream = new FileOutputStream(file)) {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

			document.open();

			paragraphTitle();
			addValuesToPdf(CityAndTemp.getCities(), CityAndTemp.getCurrentTemp(), CityAndTemp.getMaxTemp(),
					CityAndTemp.getMinTemp(), CityAndTemp.getPressure());

			// document.close();

			pdfWriter.close();
		}
	}
}
