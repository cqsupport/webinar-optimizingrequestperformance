package com.adobe.cso.tests.api.domain;

public interface Answer {
	String getId();
	
	void setId(String id);
	
	String getTitle();
	
	void setTitle(String title);
	
	String getDescription();
	
	void setDescription(String description);
	
	Question getQuestion();
	
	void setQuestion(Question question);
}
