package com.adobe.cso.tests.impl.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cso.tests.api.domain.Question;
import com.adobe.cso.tests.api.services.YahooQuestionsService;
import com.adobe.cso.tests.impl.domain.qna.AnswerImpl;
import com.adobe.cso.tests.impl.domain.qna.QuestionImpl;

@Component(immediate=true, metatype = true)
@Service
public class YahooQuestionsServiceImpl implements YahooQuestionsService {
    private static Logger logger = LoggerFactory.getLogger(YahooQuestionsServiceImpl.class);
    
    private Map<String, Object> props;
    
    public static String YAHOO_QUESTIONS_TOPICS_URL = "data.yahooquestions.topics.url";
    
    public static String YAHOO_QUESTIONS_TOPICS_URL_DEFAULT = "http://answers.yahoo.com/rss/search?p=";
    
    public static String YAHOO_QUESTIONS_SEARCHTERMS_VALUES = "data.yahooquestions.searchterms";
    
    public static String YAHOO_QUESTIONS_SEARCHTERMS_DEFAULT = "football,rugby,tennis,hockey";
    
    public static String YAHOO_QUESTIONS_QUESTION_URL = "data.yahooquestions.question.url";
    
    public static String YAHOO_QUESTIONS_QUESTION_URL_DEFAULT = "http://answers.yahoo.com/question/index?qid=";
    
    public static String YAHOO_QUESTIONS_ANSWERS_URL = "data.yahooquestions.answers.url";
    
    public static String YAHOO_QUESTIONS_ANSWERS_URL_DEFAULT = "http://answers.yahoo.com/rss/question?qid=";
    
    public static String YAHOO_QUESTIONS_MAX_RESULTS_PER_TERM = "data.yahooquestions.maxresults";
    
    public static int YAHOO_QUESTIONS_MAX_RESULTS_PER_TERM_DEFAULT = 5;
    
    @Property(
            name = "data.yahooquestions.topics.url",
            description = "The URL to call to give back the topics on a search")
    protected String yahooQuestionsTopicsUrl = "";
    
    @Property(
            name = "data.yahooquestions.searchterms",
            description = "The terms to search for in Yahoo Questions")
    protected String yahooQuestionsSearchTerms = "";
    
    @Property(
            name = "data.yahooquestions.question.url",
            description = "The URL that is the link to a particular question.  Used to pull out the ID of a question.")
    protected String yahooQuestionsQuestionUrl = "";
    
    @Property(
            name = "data.yahooquestions.answers.url",
            description = "The URL to call to get the answers to a particular topic")
    protected String yahooQuestionsAnswersUrl = "";
    
    @Property(
            name = "data.yahooquestions.maxresults",
            description = "The number of results per term to return")
    protected int yahooQuestionsMaxResultsPerTerm = 0;
    
    @Activate
    protected void activate(final Map<String, Object> props) {
    	logger.info("Activating ...");
        update(props);
    }
    
    @Modified
    protected void update(final Map<String, Object> props)
    {
    	logger.info("Modifying ...");
    	this.props = props;
    	
        String propsValueTopicsUrl = (String) this.props.get(YAHOO_QUESTIONS_TOPICS_URL);
        yahooQuestionsTopicsUrl = propsValueTopicsUrl == null ? YAHOO_QUESTIONS_TOPICS_URL_DEFAULT : propsValueTopicsUrl;
        
        String propsValueSearchTerms = (String) this.props.get(YAHOO_QUESTIONS_SEARCHTERMS_VALUES);
        yahooQuestionsSearchTerms = propsValueSearchTerms == null ? YAHOO_QUESTIONS_SEARCHTERMS_DEFAULT : propsValueSearchTerms;
        
        String propsValueQuestionUrl = (String) this.props.get(YAHOO_QUESTIONS_QUESTION_URL);
        yahooQuestionsQuestionUrl = propsValueQuestionUrl == null ? YAHOO_QUESTIONS_QUESTION_URL_DEFAULT : propsValueQuestionUrl;
        
        String propsValueAnswersUrl = (String) this.props.get(YAHOO_QUESTIONS_ANSWERS_URL);
        yahooQuestionsAnswersUrl = propsValueAnswersUrl == null ? YAHOO_QUESTIONS_ANSWERS_URL_DEFAULT : propsValueAnswersUrl;
        
        String propsValueMaxResultsSearchTermString = (String) this.props.get(YAHOO_QUESTIONS_MAX_RESULTS_PER_TERM);
        yahooQuestionsMaxResultsPerTerm = propsValueMaxResultsSearchTermString == null ? YAHOO_QUESTIONS_MAX_RESULTS_PER_TERM_DEFAULT : Integer.parseInt(propsValueMaxResultsSearchTermString);
    }
    
    @Deactivate
    protected void deactivate(ComponentContext ctx) {
    	logger.info("Deactivating ...");
    	
        this.props = null;
        yahooQuestionsTopicsUrl = null;
    }

	public String getYahooQuestionsSearchTerms() {
		return yahooQuestionsSearchTerms;
	}

	public void setYahooQuestionsSearchTerms(String yahooQuestionsSearchTerms) {
		this.yahooQuestionsSearchTerms = yahooQuestionsSearchTerms;
	}

	public int getYahooQuestionsMaxResultsPerTerm() {
		return yahooQuestionsMaxResultsPerTerm;
	}

	public void setYahooQuestionsMaxResultsPerTerm(
			int yahooQuestionsMaxResultsPerTerm) {
		this.yahooQuestionsMaxResultsPerTerm = yahooQuestionsMaxResultsPerTerm;
	}
	
	public NavigableSet<Question> retrieveQuestions()
	{
		logger.info("Starting the get of the Yahoo questions ............  ");
		
		NavigableSet<Question> questions = new TreeSet<Question>();
		String[] queries = getYahooQuestionsSearchTerms().split(",");
		
		for (String query : queries)
		{
			QuestionImpl[] queryQuestions = null;
			
			try {
				//one call per url at a time
				synchronized (yahooQuestionsTopicsUrl) {
					queryQuestions = new YahooAnswersRssReactor(yahooQuestionsTopicsUrl, 
							yahooQuestionsQuestionUrl, 
							yahooQuestionsAnswersUrl,
							query, 
							yahooQuestionsMaxResultsPerTerm)
					.retrieveQuestions();
				}

				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			questions.addAll(Arrays.asList(queryQuestions));
		}
		
		logger.info(".......... Finished get of the yahoo questions");
		
		return questions;
	}
	
	
	private class YahooAnswersRssReactor
	{
		private String topicsUrl;
		private String questionUrl;
		private String answersUrl;
		private String query;
		private Integer maxResults;
		
		public YahooAnswersRssReactor(String topicsUrl, String questionUrl, String answersUrl, String query, Integer maxResults)
		{
			this.topicsUrl = topicsUrl;
			this.questionUrl = questionUrl;
			this.answersUrl = answersUrl;
			this.query = query;
			this.maxResults = maxResults;
		}
		
		public QuestionImpl[] retrieveQuestions() throws MalformedURLException, IOException, JDOMException
		{
			QuestionImpl[] questions = new QuestionImpl[maxResults];
			
			BufferedInputStream in = new BufferedInputStream(
					new URL(getQuestionRssUrl(query, maxResults)).openStream());
			
			Document doc = new SAXBuilder(false).build(in);
			Element root = doc.getRootElement();
			
			@SuppressWarnings("rawtypes")
			List children = root.getChild("channel").getChildren("item");
			
			if (children.size() == 0) {
				return questions;
			}
			
			Integer count = 0;
			
			for (Object child: children) {
				try {
					Element eChild = (Element) child;
					
					String title = eChild.getChildText("title").trim();
					String description = eChild.getChildText("description").trim();
					String link = eChild.getChildText("link").trim();
					String answerId = StringUtils.substringAfter(link, questionUrl);
					
					int startIndex = title.indexOf("Question:");
					if (startIndex != -1) {
						title = title.substring(startIndex + "Question:".length() + 1);
					}
					
					if (description.startsWith("...")) {
						description = description.substring(3);
					}
					
					if (description.endsWith("...")) {
						description = description.substring(0, description.length()-3);
					}
					
					description = description.trim();
					
					QuestionImpl question = createQuestionTopic(title, description, answerId);
					questions[count] = question;
					
					createQuestionAnswers(question);
					
					count++;
					
				} catch (Throwable t1) {
					t1.printStackTrace();
					break;
				}
				
				if (count >= maxResults) {
					break;
				}
			}

			return questions;
		}
		
		private String getQuestionRssUrl(String q, Integer p) {
			return topicsUrl + q.replace(" ", "%20") + "&page=" + p.toString();
		}
		
		private String getAnswerRssUrl(String answerId) {
			return answersUrl + answerId;
		}
		
		private QuestionImpl createQuestionTopic(String title, String description,
	            String answerId) throws Exception
	    {
			QuestionImpl question = new QuestionImpl();
			
			question.setId(answerId);
			question.setTitle(title);
			question.setDescription(description);
			
			return question;
		}
		
		public void createQuestionAnswers(QuestionImpl question) throws Exception {		
			BufferedInputStream ansIn = null;
			
			try {
				String url = getAnswerRssUrl(question.getId());
				ansIn = new BufferedInputStream(new URL(url).openStream());
				
				Document doc = new SAXBuilder(false).build(ansIn);
				Element root = doc.getRootElement();
				
				@SuppressWarnings("rawtypes")
				List children = root.getChild("channel").getChildren("item");
				
				if (children.size() == 0) {
					return;
				}
				
				for (Object child: children) {
					try {
						Element eChild = (Element) child;
						
						String title = eChild.getChildText("title").trim();
						String description = eChild.getChildText("description").trim();
						
						AnswerImpl answer = new AnswerImpl();
						answer.setTitle(title);
						answer.setDescription(description);
						
						question.addAnswer(answer);						
					} 
					catch (Throwable t1) {
						t1.printStackTrace();
					}
				} 
			}
			catch (Throwable t2) {
				t2.printStackTrace();
			} 
			finally {
				if (ansIn != null)
					ansIn.close();
			}
		}
	}
}
