import java.util.*;

public class Testing {

  public static void main (String args[]){
    Parser testingParser = new Parser();

    // Testing generate top requests method
    assert "GET /images/WORLD-logosmall.gif HTTP/1.0 25005".equals(testingParser.generateTopRequests("NASA_access_log_Aug95", 5));
    assert "GET / HTTP/1.0 12020".equals(testingParser.generateTopRequests("NASA_access_log_Aug95", 10));
    assert "GET /images/USA-logosmall.gif HTTP/1.0 2".equals(testingParser.generateTopRequests("small_log", 5));
    assert "GET /images/ksclogosmall.gif HTTP/1.0 1".equals(testingParser.generateTopRequests("small_log", 10));
    assert "".equals(testingParser.generateTopRequests("blank_log", 5));
    assert "".equals(testingParser.generateTopRequests("blank_log", 10));
    // Testing generate response count percentage method
    assert "99.37".equals(testingParser.generateResponseCountPercentage("NASA_access_log_Aug95", true));
    assert "0.63".equals(testingParser.generateResponseCountPercentage("NASA_access_log_Aug95", false));
    assert "97.50".equals(testingParser.generateResponseCountPercentage("small_log", true));
    assert "2.50".equals(testingParser.generateResponseCountPercentage("small_log", false));
    assert "".equals(testingParser.generateResponseCountPercentage("blank_log", true));
    assert "".equals(testingParser.generateResponseCountPercentage("blank_log", false));
    // Testing generate unsuccessful requests method
    assert "GET /elv/DELTA/uncons.htm HTTP/1.0 123".equals(testingParser.generateUnsuccessfulRequests("NASA_access_log_Aug95", 5));
    assert "GET /://spacelink.msfc.nasa.gov HTTP/1.0 74".equals(testingParser.generateUnsuccessfulRequests("NASA_access_log_Aug95", 10));
    assert "GET /shuttle/missions/sts-71/video/landing.gif HTTP/1.0 1".equals(testingParser.generateUnsuccessfulRequests("small_log", 5));
    assert "GET /shuttle/missions/sts-71/video/landing.gif HTTP/1.0 1".equals(testingParser.generateUnsuccessfulRequests("small_log", 10));
    assert "".equals(testingParser.generateUnsuccessfulRequests("blank_log", 5));
    assert "".equals(testingParser.generateUnsuccessfulRequests("blank_log", 10));
    // Testing generate top hosts method
    assert "news.ti.com 1508".equals(testingParser.generateTopHosts("NASA_access_log_Aug95", 5));
    assert "piweba3y.prodigy.com 1404".equals(testingParser.generateTopHosts("NASA_access_log_Aug95", 10));
    assert "uplherc.upl.com 3".equals(testingParser.generateTopHosts("small_log", 5));
    assert "vyger312.nando.net 2".equals(testingParser.generateTopHosts("small_log", 10));
    assert "".equals(testingParser.generateTopHosts("blank_log", 5));
    assert "".equals(testingParser.generateTopHosts("blank_log", 10));

    System.out.println("All assertions passed!");
  }
}
