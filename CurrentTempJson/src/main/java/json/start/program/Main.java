package json.start.program;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.itextpdf.text.ExceptionConverter;

import json.current.temp.CityAndTemp;
import json.current.temp.CityAndTempFunctions;
import json.email.EmailFunctions;
import json.pdf.Pdf;

public class Main {

	public static Scanner input = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(Main.class);

	public static int menu() {

		System.out.println("\n");
		logger.info("\n1. -- Check the current temperature..\n"
				+ "2. -- Generate the pdf file with the entered cities and their current temperatures..\n"
				+ "3. -- Send e-mail..\n" 
				+ "0. -- Exit.. \n" 
				+ "Select one of the available options:");

		boolean continueInput = true;
		int numForMenu = 0;

		while (continueInput) {

			try {
				numForMenu = input.nextInt();
				if (numForMenu >= 0 && numForMenu <= 3) {
					continueInput = false;
				} else {
					logger.warn("Please enter the number of available options!");
				}
			} catch (InputMismatchException e) {
				logger.warn("Please enter the number of available options!");
				logger.debug(e, e);
				input.nextLine();
			}
		}
		return numForMenu;
	}

	public static void main(String[] args) {

		boolean run = true;

		while (run) {

			int checkNumMenu = menu();

			if (checkNumMenu == 0) {
				logger.info("Exiting the program..");
				run = false;
			}

			if (checkNumMenu == 1) {

				try {

					JSONArray jsonArray = new JSONArray(CityAndTempFunctions.callURL(CityAndTemp.getApiUrl()));

					CityAndTempFunctions.showCities(CityAndTempFunctions.getDataFromApi(jsonArray));
					CityAndTempFunctions.addValuesFromApi(CityAndTempFunctions.getDataFromApi(jsonArray),
							CityAndTemp.getCities(), CityAndTemp.getCurrentTemp(), CityAndTemp.getMinTemp(),
							CityAndTemp.getMaxTemp(), CityAndTemp.getPressure());
					CityAndTempFunctions.eraseDuplicate(CityAndTemp.getCities());
					CityAndTempFunctions.showCitiesAndData(CityAndTemp.getCities(), CityAndTemp.getCurrentTemp(),
							CityAndTemp.getMinTemp(), CityAndTemp.getMaxTemp(), CityAndTemp.getPressure());

				} catch (IOException e) {
					logger.error("Currently is not possible to establish a connection..Please try again later!!!");
					logger.debug(e, e);
				} catch (Exception e) {
					logger.error("An error has occurred..Please try again!!!");
					logger.debug(e, e);
				}
			}

			if (checkNumMenu == 2) {

				boolean check = true;

				while (check) {
					String name = Pdf.inputFileName();
					try {
						Pdf.createPdf(name);
						logger.info("Successfully created file..");
						check = false;
					} catch (ExceptionConverter e) {
						/*
						 * This exception is catched because in the method
						 * Pdf.createPdf(); is not set up document.close().. The
						 * reason is that we cannot generate multiple times pdf
						 * file if is closed..
						 */
						logger.info("Successfully created file..");
						check = false;
					} catch (FileAlreadyExistsException e) {
						logger.warn("A file with this name already exists!");
						continue;
					} catch (Exception e) {
						logger.error("Error when generating file..Please try again!");
						logger.debug(e, e);
						break;
					}
				}
			}

			if (checkNumMenu == 3) {

				try {
					EmailFunctions.sendEmail(EmailFunctions.toAddress(), EmailFunctions.subject(),
							EmailFunctions.message(), EmailFunctions.attachFiles());
					logger.info("Email successfully sent!");
				} catch (SendFailedException e) {
					logger.warn("E-mail address is incorrect!!Please try again!!");
				} catch (MessagingException e) {
					logger.warn("Name of attachment file is not correct!!Please try again!!");
				} catch (Exception e) {
					logger.error("An error occurred while sending e-mail!!Please try again!!");
					logger.debug(e, e);
				}

			}

		}

	}
}
