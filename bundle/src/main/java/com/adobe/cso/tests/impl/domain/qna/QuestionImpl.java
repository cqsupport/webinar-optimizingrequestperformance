package com.adobe.cso.tests.impl.domain.qna;

import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import com.adobe.cso.tests.api.domain.Answer;
import com.adobe.cso.tests.api.domain.Question;

public class QuestionImpl implements Comparable<QuestionImpl>, Question {
	private String id;
	private String title;
	private String description;
	private NavigableSet<Answer> answers;
	
	public QuestionImpl() {
		super();
		answers = new TreeSet<Answer>();
	}
	
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
	
	public Set<Answer> getAnswers() {
		return answers;
	}
	
	public void setAnswers(NavigableSet<Answer> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(Answer answer) {
		answer.setQuestion(this);
		this.answers.add(answer);
	}

	/**
	 * FIXME
	 */
	public int compareTo(QuestionImpl o) {
		return -1;
	}
}
