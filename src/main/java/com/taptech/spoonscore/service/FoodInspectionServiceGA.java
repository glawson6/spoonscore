package com.taptech.spoonscore.service;

import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import com.taptech.spoonscore.service.util.SearchUtil;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by tap on 3/26/15.
 */
@Service
public class FoodInspectionServiceGA implements FoodInspectionService {

    private final Logger log = LoggerFactory.getLogger(FoodInspectionServiceGA.class);

    private static String BIZ_RESULTS_SELECTOR = "div table tr td table tbody tr td.body div.body span strong";
    private static String RESULTS_OF_SEARCH_SELECTOR = "div table tr td table tbody tr td.body div.body b";

    private static final String GEORGIA_SUFFIX = "georgia/";
    private static String URL = "http://ga.state.gegov.com/";
    private static String URL_QUERY = URL+GEORGIA_SUFFIX+"search.cfm?start=1&1=1&f=s&r=address&s=";
    private static String URL_QUERY2 = URL+GEORGIA_SUFFIX+"search.cfm?start=1&1=1&f=s&r=name&s=";

    @Override
    public String createFoodInspectionSearchURL(RestaurantSearch restaurantSearch){

        if (null == restaurantSearch){
            throw new IllegalArgumentException("RestaurantSearch cannot be null!");
        }

        if (null == restaurantSearch.getAddress()){
            throw new IllegalArgumentException("Address cannot be null!");
        }

        if (null == restaurantSearch.getCounty()){
            throw new IllegalArgumentException("County cannot be null!");
        }

        String address = restaurantSearch.getAddress();
        String county = restaurantSearch.getCounty();

        return createSearchURL(address, county);
    }

    @Override
    public String createSearchURL(String address, String county) {
        Date now = new Date();
        StringBuilder sb = new StringBuilder();
        try {

            String firstTwotokens = SearchUtil.createInspectionSearchAddress(address);
            String addressEncoded = URLEncoder.encode(firstTwotokens, "ISO-8859-1");
            log.info("address encoded => {}", addressEncoded);
            sb.append(URL_QUERY);
            sb.append(addressEncoded);
            sb.append("&inspectionType=Food");
            sb.append("&sd=").append(SearchUtil.INSPECTION_DATE_FORMATTER.format(now));
            sb.append("&ed=").append(SearchUtil.INSPECTION_DATE_FORMATTER.format(now));
            sb.append("&useDate=NO");
            sb.append("&county=").append(county);
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding value {}", address, e);
        }

        return sb.toString();
    }

    public String createSearchURL2(String companyName, String county) {
        Date now = new Date();
        StringBuilder sb = new StringBuilder();
        try {
            String companyEncoded = URLEncoder.encode(companyName.split(" ")[0], "ISO-8859-1");
            log.info("company name => {}", companyEncoded);
            sb.append(URL_QUERY2);
            sb.append(companyEncoded);
            sb.append("&inspectionType=Food");
            sb.append("&sd=").append(SearchUtil.INSPECTION_DATE_FORMATTER.format(now));
            sb.append("&ed=").append(SearchUtil.INSPECTION_DATE_FORMATTER.format(now));
            sb.append("&useDate=NO");
            sb.append("&county=").append(county);
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding value {}", companyName, e);
        }


        return sb.toString();
    }

    @Override
    public Restaurant getInspectionResults(RestaurantSearch restaurantSearch) {

        Restaurant restaurant = new Restaurant();
        try {
            // We only want the first two tokens of the address.

            String bizResultsSelector = BIZ_RESULTS_SELECTOR;
            String resultsSelector = RESULTS_OF_SEARCH_SELECTOR;
            String url = createFoodInspectionSearchURL(restaurantSearch);
            log.debug("Getting HTML for => {} ", url);
            Document doc = Jsoup.connect(url).get();
            Elements resultsElement = doc.select(resultsSelector);
            Elements bizResultsElements = doc.select(bizResultsSelector);
            Integer results = 0;
            if (null != resultsElement){
                results = (null != resultsElement.html()) ? Integer.valueOf(resultsElement.html().split(" ")[0]) : 0;
            }
            log.debug("resultsElement.html() => {}", resultsElement.html().split(" ")[0]);
            Integer bizResultIndex = 1;
            Element foundElement = null;
            for (Element element: bizResultsElements){
                String htmlInner = element.html().toUpperCase();
                log.debug("bizResultsElements element.html().toUpperCase() => {}",htmlInner);
                if (htmlInner.contains(restaurantSearch.getCompanyName().toUpperCase())){
                    foundElement = element;
                    break;
                }
                bizResultIndex++;
            }
            log.debug("foundElement.nodeName() => {} at index {}", foundElement.nodeName(), bizResultIndex);


            String ahrefBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type("+ bizResultIndex+") + br + br + br + a";
            String brBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type("+ bizResultIndex+") + br";
            Elements ahrefBizResultsElements = doc.select(ahrefBizResultsSelector);
            Elements brBizResultsElements = doc.select(brBizResultsSelector);
            String hrefValue = null;
            String inspectionStatsString = null;
            for (Element element: ahrefBizResultsElements){
                inspectionStatsString = element.html();
                hrefValue = element.attr("href");
                log.debug("ahref html of a => [{}] href value {}",inspectionStatsString,hrefValue);
            }
            updateInspectionStats(inspectionStatsString, restaurant);
            if (log.isDebugEnabled()) {
                for (Element element : brBizResultsElements) {
                    String htmlInner = element.ownText();
                    if (element.hasText()) {
                        log.debug("brElement html of a => [{}], tagName => {}, outerHTML => {}, data => {}",
                                new Object[]{htmlInner, element.tagName(), element.outerHtml(), brBizResultsElements.toString()});
                    } else {
                        HtmlToPlainText plainText = new HtmlToPlainText();
                        String someText = plainText.getPlainText(element);
                        log.debug("Here is plain text {}", someText);
                    }
                }
            }

            String reportURLString = URL + GEORGIA_SUFFIX + hrefValue;
            log.info("Inspection report can be found at {}",reportURLString);
            restaurant.setInspectionLink(reportURLString);
            restaurant.setCompanyName(restaurantSearch.getCompanyName());
            restaurant.setCompanyAddress(restaurantSearch.getAddress());
            restaurant.getLocation().setCounty(restaurantSearch.getCounty());
            /*
            Element span = foundElement.parent();
            int loopIndex = 0;
            int foundIndex = 0;
            int cnt = 0;
            String foundAddress = null;
            for (Element spanElementChild: span.children()){
                if (foundElement.html().equals(spanElementChild.html())){

                    log.info("spanElement.tagName() => {}, html => {}, index {}",new Object[]{spanElementChild.tagName(),spanElementChild.html(), loopIndex});
                    log.info("foundElement.tagName() => {}, html => {}, index {}",new Object[]{foundElement.tagName(),foundElement.html(),loopIndex});
                    foundIndex = loopIndex;
                    break;
                }
                loopIndex++;
            }
            log.info("Index found at {}",foundIndex);
            log.info("Class at span.childNode(foundIndex) => {}",span.childNode(foundIndex+1).getClass().getName());
            String someValue = null;
            Node node = span.childNode(Math.max(foundIndex+1,span.children().size()));
            Node node2 = span.childNode(foundIndex);
            Node node3 = span.childNode(Math.min(0,foundIndex-1));
            if (node instanceof TextNode){
                TextNode textNode = (TextNode)node;
                someValue = textNode.text();
            } else {
                Element element = (Element)node;
                someValue = element.text();
                log.info("Tag name => {}",element.tagName());
            }
            log.info("foundAddress => {} at index {}",foundElement.nextSibling().unwrap(),foundIndex+1);
            log.info("Node => {}, Node2 => {}, Node3 => {}",new Object[]{node.toString(),node2.toString(),node3.toString()});
            */
        } catch (Exception e) {
            log.error("Exception ", e);
        }

        return restaurant;
    }

    @Override
    public void setInspectionResults(Restaurant restaurant, boolean isUpdate) {

        try {
            // We only want the first two tokens of the address.
            String bizResultsSelector = BIZ_RESULTS_SELECTOR;
            String resultsSelector = RESULTS_OF_SEARCH_SELECTOR;
            //String url = this.createSearchURL(restaurant.getCompanyAddress(), restaurant.getCounty());
            String url = this.createSearchURL2(restaurant.getCompanyName(), restaurant.getLocation().getCounty());
            log.debug("Getting inspection for restaurant {}",restaurant.toString());
            log.debug("Getting HTML for => {} ", url);
            restaurant.setInspectionSearchLink(url);
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36").get();
            Elements resultsElement = doc.select(resultsSelector);
            Elements bizResultsElements = doc.select(bizResultsSelector);
            Integer results = 0;
            if (null != resultsElement){
                results = (null != resultsElement.html()) ? Integer.valueOf(resultsElement.html().split(" ")[0]) : 0;
            }
            log.debug("resultsElement.html() => {}", resultsElement.html().split(" ")[0]);
            Integer bizResultIndex = 1;
            Element foundElement = null;
            try {
                for (Element element : bizResultsElements) {
                    String htmlInner = element.html().toUpperCase();
                    log.debug("bizResultsElements element.html().toUpperCase() => {}", htmlInner);
                    if (getMatchedElements(restaurant,element)) {
                        foundElement = element;
                        break;
                    }
                    bizResultIndex++;
                }
            } catch (NullPointerException e){
                log.error("Error finding element on page ",e.getMessage());
                throw e;
            } catch (Exception e){
                log.error("Error finding element on page ",e.getMessage());
                throw e;
            }
            String hrefValue = null;
            if (null != foundElement) {
                log.debug("foundElement.nodeName() => {} at index {}", foundElement.nodeName(), bizResultIndex);


                String ahrefBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type(" + bizResultIndex + ") + br + br + br + a";
                String brBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type(" + bizResultIndex + ") + br";
                Elements ahrefBizResultsElements = doc.select(ahrefBizResultsSelector);
                Elements brBizResultsElements = doc.select(brBizResultsSelector);

                String inspectionStatsString = null;
                for (Element element : ahrefBizResultsElements) {
                    inspectionStatsString = element.html();
                    hrefValue = element.attr("href");
                    log.debug("ahref html of a => [{}] href value {}", inspectionStatsString, hrefValue);
                }
                updateInspectionStats(inspectionStatsString, restaurant);
                if (log.isDebugEnabled()) {
                    for (Element element : brBizResultsElements) {
                        String htmlInner = element.ownText();
                        if (element.hasText()) {
                            log.debug("brElement html of a => [{}], tagName => {}, outerHTML => {}, data => {}",
                                    new Object[]{htmlInner, element.tagName(), element.outerHtml(), brBizResultsElements.toString()});
                        } else {
                            HtmlToPlainText plainText = new HtmlToPlainText();
                            String someText = plainText.getPlainText(element);
                            log.debug("Here is plain text {}", someText);
                        }
                    }
                }
            }

            if (null != hrefValue) {
                String reportURLString = URL + GEORGIA_SUFFIX + hrefValue;
                log.info("Inspection report can be found at {}", reportURLString);
                restaurant.setInspectionLink(reportURLString);
                getReportLink(restaurant);
            }

        } catch (NullPointerException e){
            log.error("Error finding element on page ",e.getMessage());
        } catch (Exception e) {
            log.error("Exception ", e);
        }

    }

    private boolean getMatchedElements(Restaurant restaurant, Element sampleElement){
        boolean matched = false;
        String htmlInner = sampleElement.html().toUpperCase();
        if (htmlInner.contains(restaurant.getCompanyName().toUpperCase())){
            return true;
        }
        if (restaurant.getCompanyName().toUpperCase().contains(htmlInner)){
            return true;
        }
        return matched;
    }

    private static final String REPORT_LINK_SELECTOR = "div table tbody tr td table tbody tr td.body div.body table tbody tr td.body a";
    private void getReportLink(Restaurant restaurant) {
        try{
            String url = restaurant.getInspectionLink();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36").get();
            Elements reportLinkElements = doc.select(REPORT_LINK_SELECTOR);
            Element reportLinkElement = reportLinkElements.first();
            String hrefValue = reportLinkElement.attr("href");
            String htmlInner = reportLinkElement.html();
            String reportURL = URL + hrefValue;
            log.info("Report Link {}",reportURL);
            restaurant.setViewReportLink(reportURL);
        } catch (Exception e){

        }
    }

    // String should look like this => August 12, 2013 Score: 97, Grade: A
    private void updateInspectionStats(String inspectionStatsString, Restaurant restaurant) {
        String [] tokens = inspectionStatsString.split(",");
        String grade = tokens[2].substring(tokens[2].length() - 2);
        String score = tokens[1].substring(tokens[1].length() - 3);
        restaurant.setCompanyInspectionGrade(grade.trim());
        restaurant.setCompanyInspectionScore(score.trim());
    }



    public void testHTMLParsing(){
        try {
            String bizName = "Sway";
            String address = "265 Peachtree St NE Atlanta, GA 30308";
            String [] addressTokens = address.split(" ");
            String firstTwotokens = addressTokens[0]+" "+addressTokens[1];
            String addressEncoded = URLEncoder.encode(firstTwotokens, "UTF-8");
            String testAddress2 = SearchUtil.createInspectionSearchAddress(address);
            log.info("testAddress2 => {}",testAddress2);
            log.info("address encoded => {}",addressEncoded);
            String bizResultsSelector = "div table tr td table tbody tr td.body div.body span strong";
            String resultsSelector = "div table tr td table tbody tr td.body div.body b";
            //String url = "http://ga.state.gegov.com/georgia/search.cfm?start=21&1=1&f=s&r=address&s=265%20Peachtree&inspectionType=Food&sd=02/23/2015&ed=03/25/2015&useDate=NO&county=Fulton&";

            String url = "http://ga.state.gegov.com/georgia/search.cfm?start=1&1=1&f=s&r=address&s="+addressEncoded+"&inspectionType=Food&sd=02/23/2015&ed=03/25/2015&useDate=NO&county=Fulton&";

            Document doc = null;
            doc = Jsoup.connect(url).get();
            Elements resultsElement = doc.select(resultsSelector);
            Elements bizResultsElements = doc.select(bizResultsSelector);
            Integer results = 0;
            if (null != resultsElement){
                results = (null != resultsElement.html()) ? Integer.valueOf(resultsElement.html().split(" ")[0]) : 0;
            }
            log.info("resultsElement.html() => {}", resultsElement.html().split(" ")[0]);
            log.info("bizResultsElements");
            Integer bizResultIndex = 1;
            Element foundElement = null;
            for (Element element: bizResultsElements){
                String htmlInner = element.html().toUpperCase();
                log.info("htmlInner => {}",htmlInner);
                if (htmlInner.contains(bizName.toUpperCase())){
                    foundElement = element;
                    break;
                }
                bizResultIndex++;
            }
            log.info("foundElement.nodeName() => {} at index {}",foundElement.nodeName(),bizResultIndex);


            String ahrefBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type("+ bizResultIndex+") + br + br + br + a";
            String brBizResultsSelector = "div table tr td table tbody tr td.body div.body span strong:nth-of-type("+ bizResultIndex+") + br";
            Elements ahrefBizResultsElements = doc.select(ahrefBizResultsSelector);
            Elements brBizResultsElements = doc.select(brBizResultsSelector);
            for (Element element: ahrefBizResultsElements){
                String htmlInner = element.html();
                String hrefValue = element.attr("href");
                log.info("ahref html of a => {} href value {}",htmlInner,hrefValue);
            }
            for (Element element: brBizResultsElements){
                String htmlInner = element.ownText();
                if (element.hasText()) {
                    log.info("brElement html of a => [{}], tagName => {}, outerHTML => {}, data => {}",
                            new Object[]{htmlInner, element.tagName(), element.outerHtml(), brBizResultsElements.toString()});
                } else {
                    log.info("What is this?");
                    HtmlToPlainText plainText = new HtmlToPlainText();
                    String someText = plainText.getPlainText(element);
                    log.info("Here is plain text {}",someText);
                }
            }
            Element span = foundElement.parent();
            int loopIndex = 0;
            int foundIndex = 0;
            int cnt = 0;
            String foundAddress = null;
            for (Element spanElementChild: span.children()){
                if (foundElement.html().equals(spanElementChild.html())){

                    log.info("spanElement.tagName() => {}, html => {}, index {}",new Object[]{spanElementChild.tagName(),spanElementChild.html(), loopIndex});
                    log.info("foundElement.tagName() => {}, html => {}, index {}",new Object[]{foundElement.tagName(),foundElement.html(),loopIndex});
                    foundIndex = loopIndex;
                    break;
                }
                loopIndex++;
            }
            log.info("Index found at {}",foundIndex);
            log.info("Class at span.childNode(foundIndex) => {}",span.childNode(foundIndex+1).getClass().getName());
            String someValue = null;
            Node node = span.childNode(Math.max(foundIndex+1,span.children().size()));
            Node node2 = span.childNode(foundIndex);
            Node node3 = span.childNode(Math.min(0,foundIndex-1));
            if (node instanceof TextNode){
                TextNode textNode = (TextNode)node;
                someValue = textNode.text();
            } else {
                Element element = (Element)node;
                someValue = element.text();
                log.info("Tag name => {}",element.tagName());
            }
            log.info("foundAddress => {} at index {}",foundElement.nextSibling().unwrap(),foundIndex+1);
            log.info("Node => {}, Node2 => {}, Node3 => {}",new Object[]{node.toString(),node2.toString(),node3.toString()});
            /*
            for (TextNode textNode:span.textNodes()){
                log.info("Text in span => {}",textNode.text());
                if (cnt == foundIndex){
                    foundAddress = textNode.text();
                    log.info("foundAddress => {}",foundAddress);
                }
                cnt++;
            }
            */
            //log.info("foundElement.getElementsByIndexEquals({}).html() => {}",new Object[]{index+4,afterElements.toString()});
        } catch (Exception e) {
            log.error("Exception ",e);
            e.printStackTrace();
        }

    }
}
