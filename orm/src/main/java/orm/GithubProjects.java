package orm;

import java.util.ArrayList;
import java.util.List;

public class GithubProjects {
	private int id;
	private String repositoryURL;
	private String fileURL;
	private int noOfCommits;
	private int noOfCollaborators;
	private int noOfForks;
	private int listOfOpenIssues;
	private int listOfClosedIssues;
	private List<CommitDetails> stockDailyRecords = new ArrayList<CommitDetails>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public int getNoOfCommits() {
		return noOfCommits;
	}

	public void setNoOfCommits(int noOfCommits) {
		this.noOfCommits = noOfCommits;
	}

	public int getNoOfCollaborators() {
		return noOfCollaborators;
	}

	public void setNoOfCollaborators(int noOfCollaborators) {
		this.noOfCollaborators = noOfCollaborators;
	}

	public int getNoOfForks() {
		return noOfForks;
	}

	public void setNoOfForks(int noOfForks) {
		this.noOfForks = noOfForks;
	}

	public int getListOfOpenIssues() {
		return listOfOpenIssues;
	}

	public void setListOfOpenIssues(int listOfOpenIssues) {
		this.listOfOpenIssues = listOfOpenIssues;
	}

	public int getListOfClosedIssues() {
		return listOfClosedIssues;
	}

	public void setListOfClosedIssues(int listOfClosedIssues) {
		this.listOfClosedIssues = listOfClosedIssues;
	}

	public List<CommitDetails> getStockDailyRecords() {
		return stockDailyRecords;
	}

	public void setStockDailyRecords(List<CommitDetails> stockDailyRecords) {
		this.stockDailyRecords = stockDailyRecords;
	}

}
