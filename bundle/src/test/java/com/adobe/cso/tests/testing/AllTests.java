package com.adobe.cso.tests.testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.adobe.cso.tests.impl.domain.crm.CustomerUnitTest;
import com.adobe.cso.tests.impl.domain.media.VideoIntegrationTest;
import com.adobe.cso.tests.impl.services.CustomerDataDeduplicaterServiceIntegrationTest;
import com.adobe.cso.tests.impl.services.VideoMetaDataServiceIntegrationTest;
import com.adobe.cso.tests.impl.services.YahooQuestionsServiceIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses( { CustomerUnitTest.class, VideoIntegrationTest.class, CustomerDataDeduplicaterServiceIntegrationTest.class, VideoMetaDataServiceIntegrationTest.class, YahooQuestionsServiceIntegrationTest.class  })
public class AllTests {

}
