package com.adobe.cso.tests.api.domain;

import java.util.NavigableSet;
import java.util.Set;

public interface Question {
	String getId();
	
	void setId(String id);
	
	String getTitle();
	
	void setTitle(String title);
	
	String getDescription();
	
	void setDescription(String description);
	
	public Set<Answer> getAnswers();
	
	public void setAnswers(NavigableSet<Answer> answers);
	
	public void addAnswer(Answer answer);
}
