package com.adobe.cso.tests.api.services;

import java.util.NavigableSet;

import com.adobe.cso.tests.api.domain.Question;

public interface YahooQuestionsService {
	String getYahooQuestionsSearchTerms();

	void setYahooQuestionsSearchTerms(String yahooQuestionsSearchTerms);

	int getYahooQuestionsMaxResultsPerTerm() ;

	void setYahooQuestionsMaxResultsPerTerm(
			int yahooQuestionsMaxResultsPerTerm);
	
	NavigableSet<Question> retrieveQuestions();
}
