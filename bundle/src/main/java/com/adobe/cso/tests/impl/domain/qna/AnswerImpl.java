package com.adobe.cso.tests.impl.domain.qna;

import com.adobe.cso.tests.api.domain.Answer;
import com.adobe.cso.tests.api.domain.Question;

public class AnswerImpl implements Comparable<Answer>, Answer {
	private String id;
	private String title;
	private String description;
	private Question question;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * FIXME
	 */
	public int compareTo(Answer o) {
		return -1;
	}
}
