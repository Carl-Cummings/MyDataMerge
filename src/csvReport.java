import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat; //Used To Format Dates
import java.util.Date; //Used for Date data type
import java.util.Comparator; //Used To Sort csvReport

public class csvReport {
    String clientAddress;
    String clientGuid;
    Date requestTime;
    String serviceGuid;
    int retriesRequest;
    int packetsRequested;
    int packetsServiced;
    int maxHoleSize;

    public csvReport() {
        super();
    }

    public csvReport(String clientAddress, String clientGuid, Date requestTime, String serviceGuid, int retriesRequest, int packetsRequested, int packetsServiced, int maxHoleSize) {
        this.clientAddress = clientAddress;
        this.clientGuid = clientGuid;
        this.requestTime = requestTime;
        this.serviceGuid = serviceGuid;
        this.retriesRequest = retriesRequest;
        this.packetsRequested = packetsRequested;
        this.packetsServiced = packetsServiced;
        this.maxHoleSize = maxHoleSize;
    }

    @JsonProperty("client-address") public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @JsonProperty("client-guid") public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    @JsonProperty("request-time") public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    @JsonProperty("service-guid") public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    @JsonProperty("retries-request") public int getRetriesRequest() {
        return retriesRequest;
    }

    public void setRetriesRequest(int retriesRequest) {
        this.retriesRequest = retriesRequest;
    }

    @JsonProperty("packets-requested") public int getPacketsRequested() {
        return packetsRequested;
    }

    public void setPacketsRequested(int packetsRequested) {
        this.packetsRequested = packetsRequested;
    }

    @JsonProperty("packets-serviced") public int getPacketsServiced() {
        return packetsServiced;
    }

    public void setPacketsServiced(int packetsServiced) {
        this.packetsServiced = packetsServiced;
    }

    @JsonProperty("max-hole-size") public int getMaxHoleSize() {
        return maxHoleSize;
    }

    public void setMaxHoleSize(int maxHoleSize) {
        this.maxHoleSize = maxHoleSize;
    }

    //Used to sort the csvClass
    public static Comparator<csvReport> csvReportComparator = new Comparator<>() {

        public int compare(csvReport s1, csvReport s2) {
            Date requestTime1 = s1.requestTime;
            Date requestTime2 = s2.requestTime;

            //ascending order
            return requestTime1.compareTo(requestTime2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public static csvReport createCsvReport(String[] values) {
        String pattern = "yyyy-MM-dd HH:mm:ss z";
        SimpleDateFormat mySimpleDateformat = new SimpleDateFormat(pattern);

        String clientAddress = values[0];
        String clientGuid = values[1];
        Date requestTime = null;
        try {
            requestTime = mySimpleDateformat.parse(values[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String serviceGuid = values[3];
        int retriesRequest = Integer.parseInt(values[4]);
        int packetsRequested = Integer.parseInt(values[5]);
        int packetsServiced = Integer.parseInt(values[6]);
        int maxHoleSize = Integer.parseInt(values[7]);

        return new csvReport(clientAddress, clientGuid, requestTime, serviceGuid, retriesRequest, packetsRequested, packetsServiced, maxHoleSize);
    }

    public static String PrintRecordToFile(csvReport csvReportRec) {
        String pattern = "yyyy-MM-dd HH:mm:ss z";
        SimpleDateFormat mySimpleDateformat = new SimpleDateFormat(pattern);

        return csvReportRec.clientAddress + "," + csvReportRec.clientGuid + "," + mySimpleDateformat.format(csvReportRec.requestTime)
                + "," + csvReportRec.serviceGuid + "," + csvReportRec.retriesRequest + "," + csvReportRec.packetsRequested
                + "," + csvReportRec.packetsServiced + "," + csvReportRec.maxHoleSize;
    }

    //Used to Print the class out
    @Override
    public String toString() {
        String pattern = "yyyy-MM-dd HH:mm:ss z";
        SimpleDateFormat mySimpleDateformat = new SimpleDateFormat(pattern);

        return "cvsReport [clientAddress: " + clientAddress + ", clientGuid: " + clientGuid + ", requestTime: " + mySimpleDateformat.format(requestTime)
                + ", serviceGuid: " + serviceGuid + ", retriesRequest: " + retriesRequest + ", packetsRequested: " + packetsRequested
                + ", packetsServiced: " + packetsServiced + ", maxHoleSize: " + maxHoleSize + "]";
    }
}
