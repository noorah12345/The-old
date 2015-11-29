package com.github.dvdme.ForecastIOLib;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FIOLibTest {

    private static final String apikey = "77ccedf783c65b4aa615e5a0dfa627dc";

    public static void main(String[] args) throws MalformedURLException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
URL myUrl = new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=Riyadh&sensor=false");
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(myUrl.openStream());
XPathFactory xPathfactory = XPathFactory.newInstance();
XPath xpath = xPathfactory.newXPath();
XPathExpression expr = xpath.compile("/GeocodeResponse/result/geometry/location/lat");
String swLat = expr.evaluate(doc, XPathConstants.STRING).toString();
System.out.println("swLat: " + swLat );

XPathExpression expr2 = xpath.compile("/GeocodeResponse/result/geometry/location/lng");
String swLang = expr2.evaluate(doc, XPathConstants.STRING).toString();
        //Some coordinates for testing
        //Lisbon:   38.7252993 , -9.1500364
        //Madrid:   40.41678 , -3.70379
        //Ceuta:    35.88838 , -5.32464
        //Paris:    48.85661 , 2.35222 
        //Berlin:   52.51917 , 13.40609
        //Brasilia: -15.83454 , -47.98828
        //London:   51.51121 , -0.11982
        //Alcatraz: 37.8267 , -122.423
        //Caracas:  10.4880555, -66.8791667
        ForecastIO fio = new ForecastIO(apikey);
        fio.setUnits(ForecastIO.UNITS_SI);
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.getForecast(swLat, swLang);

        //Response Headers info
        System.out.println("Response Headers:");
        System.out.println("Cache-Control: " + fio.getHeaderCache_Control());
        System.out.println("Expires: " + fio.getHeaderExpires());
        System.out.println("X-Forecast-API-Calls: " + fio.getHeaderX_Forecast_API_Calls());
        System.out.println("X-Response-Time: " + fio.getHeaderX_Response_Time());
        System.out.println("\n");

        //ForecastIO info
        System.out.println("Latitude: " + fio.getLatitude());
        System.out.println("Longitude: " + fio.getLongitude());
        System.out.println("Timezone: " + fio.getTimezone());
        System.out.println("Offset: " + fio.offsetValue());
        System.out.println("\n");

        //Currently data
        FIOCurrently currently = new FIOCurrently(fio);

        System.out.println("\nCurrently\n");
        String[] f = currently.get().getFieldsArray();
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i] + ": " + currently.get().getByKey(f[i]));
        }
        System.out.println("\n");

        //Minutely data
        FIOMinutely minutely = new FIOMinutely(fio);
        if (minutely.minutes() < 0) {
            System.out.println("No minutely data.");
        } else {
            System.out.println("\nMinutely\n");
        }
        for (int i = 0; i < minutely.minutes(); i++) {
            String[] m = minutely.getMinute(i).getFieldsArray();
            System.out.println("Minute #" + (i + 1));
            for (int j = 0; j < m.length; j++) {
                System.out.println(m[j] + ": " + minutely.getMinute(i).getByKey(m[j]));
            }
            System.out.println("\n");
        }

        //Hourly data
        FIOHourly hourly = new FIOHourly(fio);
        if (hourly.hours() < 0) {
            System.out.println("No hourly data.");
        } else {
            System.out.println("\nHourly:\n");
        }
        for (int i = 0; i < hourly.hours(); i++) {
            String[] h = hourly.getHour(i).getFieldsArray();
            System.out.println("Hour #" + (i + 1));
            for (int j = 0; j < h.length; j++) {
                System.out.println(h[j] + ": " + hourly.getHour(i).getByKey(h[j]));
            }
            System.out.println("\n");
        }

        //Daily data
        FIODaily daily = new FIODaily(fio);
        if (daily.days() < 0) {
            System.out.println("No daily data.");
        } else {
            System.out.println("\nDaily:\n");
        }
        for (int i = 0; i < daily.days(); i++) {
            String[] h = daily.getDay(i).getFieldsArray();
            System.out.println("Day #" + (i + 1));
            for (int j = 0; j < h.length; j++) {
                System.out.println(h[j] + ": " + daily.getDay(i).getByKey(h[j]));
            }
            System.out.println("\n");
        }

        //Flags data
        FIOFlags flags = new FIOFlags(fio);
        System.out.println("Available Flags: ");
        for (int i = 0; i < flags.availableFlags().length; i++) {
            System.out.println(flags.availableFlags()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.metarStations().length; i++) {
            System.out.println("Metar Station: " + flags.metarStations()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.isdStations().length; i++) {
            System.out.println("ISD Station: " + flags.isdStations()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.sources().length; i++) {
            System.out.println("Source: " + flags.sources()[i]);
        }
        System.out.println("\n");
        System.out.println("Units: " + flags.units());
        System.out.println("\n");

        //Alerts data
        FIOAlerts alerts = new FIOAlerts(fio);
        if (alerts.NumberOfAlerts() <= 0) {
            System.out.println("No alerts for this location.");
        } else {
            System.out.println("Alerts");
            for (int i = 0; i < alerts.NumberOfAlerts(); i++) {
                System.out.println(alerts.getAlert(i));
            }
        }

    }//main

}
