package com.adobe.cso.tests.impl.services;

import java.util.HashMap;
import java.util.NavigableSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.adobe.cso.tests.api.domain.Question;

/**
 * Not a full CQ integration test, just a test to ensure connectivity to YahooQuestions 
 * and parsing of the RSS feeds works.
 */
public class YahooQuestionsServiceIntegrationTest{

	public static String TEST_SEARCH_TERMS = "football";
	public static int TEST_QUESTIONS_COUNT = 5;
	
	private YahooQuestionsServiceImpl questionsService;
	
	@Before
	public void setUp()
	{
		//Just test the questions service directly, not in OSGi container
		questionsService = new YahooQuestionsServiceImpl();
		
		//Use the default properties of the service
		questionsService.activate(new HashMap<String, Object>());
	}
	
	@After
	public void tearDown()
	{
		questionsService = null;
	}
	
	@Test
	public void testDefaultPropertiesSet()
	{
		Assert.assertTrue("default search terms set", questionsService.getYahooQuestionsSearchTerms().equals(YahooQuestionsServiceImpl.YAHOO_QUESTIONS_SEARCHTERMS_DEFAULT));
		Assert.assertTrue("default max results", questionsService.getYahooQuestionsMaxResultsPerTerm() == YahooQuestionsServiceImpl.YAHOO_QUESTIONS_MAX_RESULTS_PER_TERM_DEFAULT);
	}
	
	@Test
	public void testGetQuestions()
	{
		questionsService.setYahooQuestionsSearchTerms(TEST_SEARCH_TERMS);
		questionsService.setYahooQuestionsMaxResultsPerTerm(TEST_QUESTIONS_COUNT);
		
		NavigableSet<Question> questions = questionsService.retrieveQuestions();
		
		Assert.assertTrue("correct number of questions returned for max results", questions.size() == TEST_QUESTIONS_COUNT);
		Assert.assertTrue("a question has some answers (note, a question _might_ not have answers, but unlikely in Yahoo Questions!!)", questions.first().getAnswers().size() > 0);
	}
}
