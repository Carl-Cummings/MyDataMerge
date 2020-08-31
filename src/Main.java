
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Needed To Read the File and CatchExceptions
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

//Needed To Convert the String to date

//Needed To Hold the ReportList and Sort it
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        File myCSVFile = new File("reports.csv");
        File myXMLFile = new File("reports.xml");
        File myJSONFile = new File("reports.json");
        ArrayList<csvReport> csvReports = new ArrayList<>();
        Set<String> serviceGuidHashSet = new HashSet<>();
        String line = "";
        String headerLine = "";

        //Processing csvFile First
        try {
            //Open File and get it ready to be read
            BufferedReader br = new BufferedReader(new FileReader(myCSVFile));

            //Used to get the headerline to use later when we write out to new csv file
            headerLine = br.readLine();

            //For Each line read split it on comma then Create Report object and add it to the Array
            while((line = br.readLine()) != null) {
                String [] values = line.split(",");
                if (Integer.parseInt(values[6]) == 0) {
                    continue;
                }
                serviceGuidHashSet.add(values[3]);
                csvReport csvReportLine = csvReport.createCsvReport(values);
                csvReports.add(csvReportLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Processing XML File Second
        try {
            DocumentBuilderFactory myDBFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder myDBuilder = myDBFactory.newDocumentBuilder();
            Document myDoc = myDBuilder.parse(myXMLFile);

            //Read array of elements
            NodeList myNodeList = myDoc.getElementsByTagName("report");

            //Process the node list
            for (int i=0; i < myNodeList.getLength(); i++) {
                Node myNode = myNodeList.item(i);

                if(myNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element myElement = (Element) myNode;
                    String[] myCVSMapping = new String[8];

                    myCVSMapping[0] = myElement.getElementsByTagName("client-address").item(0).getTextContent();
                    myCVSMapping[1] = myElement.getElementsByTagName("client-guid").item(0).getTextContent();
                    myCVSMapping[2] = myElement.getElementsByTagName("request-time").item(0).getTextContent();
                    myCVSMapping[3] = myElement.getElementsByTagName("service-guid").item(0).getTextContent();
                    myCVSMapping[4] = myElement.getElementsByTagName("retries-request").item(0).getTextContent();
                    myCVSMapping[5] = myElement.getElementsByTagName("packets-requested").item(0).getTextContent();
                    myCVSMapping[6] = myElement.getElementsByTagName("packets-serviced").item(0).getTextContent();
                    myCVSMapping[7] = myElement.getElementsByTagName("max-hole-size").item(0).getTextContent();

                    if (Integer.parseInt(myCVSMapping[6]) == 0) {
                        continue;
                    }

                    serviceGuidHashSet.add(myCVSMapping[3]);
                    csvReport csvReportLine = csvReport.createCsvReport(myCVSMapping);
                    csvReports.add(csvReportLine);
                }

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //Processing JSON File Third
        ObjectMapper myObjMap = new ObjectMapper();
        JsonFactory myJsonFactory = new JsonFactory();
        try {
            JsonParser myJsonParser = myJsonFactory.createParser(myJSONFile);
            TypeReference<List<csvReport>> myTypeRef = new TypeReference<>() {};
            List<csvReport> myJsonReportList = myObjMap.readValue(myJsonParser, myTypeRef);

            for (csvReport c : myJsonReportList) {
                if (c.packetsServiced == 0){
                    continue;
                }
                serviceGuidHashSet.add(c.serviceGuid);
                csvReports.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Now that we have all the Report Lines we can sort it
        csvReports.sort(csvReport.csvReportComparator);

        //Now we can write out the merged data to new csv file
        File myNewCsvFile = new File("newreport.csv");
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter(myNewCsvFile, true));
            //Print Out the headerline
            myWriter.write(headerLine);
            myWriter.newLine();
            for (csvReport v : csvReports) {
                //System.out.println(v);
                myWriter.write(csvReport.PrintRecordToFile(v));
                myWriter.newLine();
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //This is ued for testing if everything looks ok.
        //for (csvReport a : csvReports) {
        //    System.out.println(a);
        //}

        //This Prints a Summary of the Service GUID and its Counts
        int totalCount = 0;
        for (String b : serviceGuidHashSet) {
            int serviceGuidCount = 0;
            for (csvReport c : csvReports) {
                if (b.equals(c.serviceGuid)) {
                    serviceGuidCount++;
                }
            }
            System.out.println(b + " - " + serviceGuidCount);
            totalCount += serviceGuidCount;
        }

        System.out.println("Total Records: " + totalCount);
    }
}
