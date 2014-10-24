package orm;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Java_GitHub_Projects")
public class JavaGithubProjects extends GithubProjects{
	@Id
	@GeneratedValue(generator = "javaID_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "javaID_seq", name = "javaID_seq")
	private int id;
	@Column(length=4000)
	private String repositoryURL;
	@Column(length=4000)
	private String fileURL;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
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
	public List<CommitDetails> getStockDailyRecords() {
		return stockDailyRecords;
	}

	public void setStockDailyRecords(List<CommitDetails> stockDailyRecords) {
		this.stockDailyRecords = stockDailyRecords;
	}

}
