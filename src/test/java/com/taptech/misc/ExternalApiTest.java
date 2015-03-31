package com.taptech.misc;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.service.util.SearchUtil;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URLEncoder;

/**
 * Created by tap on 3/26/15.
 */
public class ExternalApiTest {


    private final Logger log = LoggerFactory.getLogger(ExternalApiTest.class);
    @Test
    public void testHTMLParsing(){
        try {
            String bizName = "Sway";
            String address = "265 Peachtree St NE Atlanta, GA 30308";
            String [] addressTokens = address.split(" ");
            String firstTwotokens = addressTokens[0]+" "+addressTokens[1];
            String addressEncoded = URLEncoder.encode(firstTwotokens, "UTF-8");
            String testAddress2 = SearchUtil.createInspectionSearchAddress(address);
            String testAddress2Encoded = SearchUtil.encodeString(testAddress2);
            log.info("testAddress2 => {}, encoded => {}",testAddress2,testAddress2Encoded);
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
    public static void main (String args[]){
        System.out.println("Hello world!");
    }
}
