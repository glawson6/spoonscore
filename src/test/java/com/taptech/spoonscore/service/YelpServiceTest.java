package com.taptech.spoonscore.service;

import com.taptech.spoonscore.Application;
import com.taptech.spoonscore.domain.Restaurant;
import com.taptech.spoonscore.domain.RestaurantSearch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see com.taptech.spoonscore.service.UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class YelpServiceTest {

    private final Logger log = LoggerFactory.getLogger(YelpService.class);

    @Inject
    private Environment env;

    @Inject
    YelpService yelpService;

    @Inject
    @Qualifier("YelpSearchService")
    CompanySearch companySearch;

    @Test
    public void testSanity(){
        log.info("We are sane!!!!!!");
    }

    @Test
    public void testYelpSearchService(){
        log.info("We need a test!");
        Collection<Restaurant> restaurants = companySearch.findCompanyLocations(new RestaurantSearch());
    }

    @Test
     public void testYelpCall(){
        String term = "food";
        //String location = "Atlanta";
        String location = "30315";
        String response = yelpService.testQueryAPI(term,location);
        log.info("Response returned => {}",response);
    }

    @Test
    public void testHTMLParsing(){
        try {
            String bizName = "Sway";
            String address = "265 Peachtree St NE Atlanta, GA 30308";
            String [] addressTokens = address.split(" ");
            String firstTwotokens = addressTokens[0]+" "+addressTokens[1];
            String addressEncoded = URLEncoder.encode(firstTwotokens, "UTF-8");
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
            Element foundElement = null;
            for (Element element: bizResultsElements){
                String htmlInner = element.html().toUpperCase();
                log.info("htmlInner => {}",htmlInner);
                if (htmlInner.contains(bizName.toUpperCase())){
                    foundElement = element;
                    break;
                }
            }
            log.info("foundElement.nodeName() => {}",foundElement.nodeName());
            log.info("foundElement.nodeName() => {}",foundElement.nextElementSibling().nodeName());
            int index = foundElement.siblingIndex();
            log.info("index => {}",index);
            boolean continueLooping = foundElement.nextElementSibling() != null;
            int cnt = 1;
            while (continueLooping){
                Element element = foundElement.nextElementSibling();
                String htmlInner = element.html().toUpperCase();
                String nodeName = element.nodeName();
                log.info("htmlInner[{}] => {}",new Object[]{cnt,htmlInner});
                cnt++;
                if (null == element || cnt >= 10){
                    continueLooping = false;
                }
            }
            //log.info("foundElement.getElementsByIndexEquals({}).html() => {}",new Object[]{index+4,afterElements.toString()});
        } catch (Exception e) {
            log.error("Exception ",e);
            e.printStackTrace();
        }

    }
}
