package orm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHCommit.ShortInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Crawler {

	private static void printHits(WebDriver driver, String language)
			throws IOException {
		String xpath = "//div[@class='code-list']/*";
		List<WebElement> hits = driver.findElements(By.xpath(xpath));
		Iterator<WebElement> iter = hits.iterator();
		while (iter.hasNext()) {
			// System.out.println(iter.next().getText());
			xpath = "//p[@class='title']/a";
			List<WebElement> links = (List<WebElement>) driver.findElements(By
					.xpath(xpath));
			String repositoryURL = links.get(0).getAttribute("href");
			String fileURL = links.get(1).getAttribute("href");
			System.out.println(repositoryURL + " " + fileURL+" "+repositoryURL.replaceAll("https://github.com/", ""));
			List list = getGitHubDetails(
					repositoryURL.replaceAll("https://github.com/", ""),
					language);
			saveDetail(repositoryURL, fileURL, list);
			break;
		}
	}

	private static List getGitHubDetails(String repositoryName, String language)
			throws IOException {
		List result = new ArrayList();
		GitHub github = GitHub.connect();
		GHRepository r = github.getRepository(repositoryName);
		int noOfCollaborators = r.listCollaborators().asList().size();
		int noOfCommits = r.listCommits().asList().size();
		int noOfForks = r.getForks();
		int openIssues = r.listIssues(GHIssueState.OPEN).asList().size();
		int closedIssues = r.listIssues(GHIssueState.CLOSED).asList().size();
		result.add(language);
		result.add(noOfCollaborators);
		result.add(noOfCommits);
		result.add(noOfForks);
		result.add(openIssues);
		result.add(closedIssues);
		result.add(getCommitDetails(r));
		return result;
	}

	private static List<CommitDetails> getCommitDetails(GHRepository r)
			throws IOException {
		List<CommitDetails> list = new ArrayList<CommitDetails>();
		PagedIterable<GHCommit> commits = r.listCommits();
		for (GHCommit commit : commits) {
			CommitDetails details = new CommitDetails();
			if (commit != null) {
				details.setSha(commit.getSHA1());

				ShortInfo info = commit.getCommitShortInfo();
				//System.out.println("Comment Count " + info.getCommentCount());
				//System.out.println("Message" + info.getMessage());
				details.setAuthor(info.getAuthor().getName());
				details.setAuthorEmailAddress(info.getAuthor().getEmail());
				details.setAuthorDate(info.getAuthor().getDate());

				details.setCommitter(info.getCommitter().getName());
				details.setCommitterDate(info.getCommitter().getDate());
				details.setCommitterEmailAddress(info.getCommitter().getEmail());

				String parentShas = "";
				for (String p : commit.getParentSHA1s())
					parentShas += p + ",";
				details.setParentSha(parentShas.substring(0,
						parentShas.length()));

				List<File> files = r.getCommit(commit.getSHA1()).getFiles();
				//System.out.println("Files Size " + files.size());
				for (File file : files) {

					FileCommitDetails commitDetails = new FileCommitDetails();
					commitDetails.setFileName(file.getFileName());
					commitDetails.setLinesAdded(file.getLinesAdded());
					commitDetails.setLinesChanged(file.getLinesChanged());
					commitDetails.setLinesRemoved(file.getLinesDeleted());
					commitDetails.setPatch(file.getPatch());
					details.getFileCommitDetails().add(commitDetails);
				}

			}
			list.add(details);
		}

		return list;
	}

	private static void saveDetail(String repositoryURL, String fileURL,
			List list) {
		Session session = null;
		GithubProjects bean = null;
		switch (list.get(0).toString()) {
		case "java":
			bean = new JavaGithubProjects();

			break;
		case "csharp":
			bean = new CsharpGithubProjects();
			break;
		case "php":
			bean = new PHPGithubProjects();
			break;
		case "python":
			bean = new PythonGithubProjects();
			break;
		}
		bean.setFileURL(fileURL);
		bean.setRepositoryURL(repositoryURL);
		bean.setListOfClosedIssues(Integer.parseInt(list.get(5).toString()));
		bean.setListOfOpenIssues(Integer.parseInt(list.get(4).toString()));
		bean.setNoOfCollaborators(Integer.parseInt(list.get(1).toString()));
		bean.setNoOfCommits(Integer.parseInt(list.get(2).toString()));
		bean.setNoOfForks(Integer.parseInt(list.get(3).toString()));

		bean.setStockDailyRecords((List<CommitDetails>) list.get(6));
		try {
			session = DatabaseConfig.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(bean);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			for (Throwable ex = e; ex != null; ex = e.getCause())
				ex.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		}
	}

	private static void fetchURL(WebDriver driver, String url) {
		driver.get(url);
		List<WebElement> hls = driver.findElements(By.xpath("//h1"));
		Iterator<WebElement> iter = hls.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getText());
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		process("java", "import javax.persistence.Entity");
		process("csharp", "nhibernate");
		process("php", "use Doctrine");
		 process("python","from sqlalchemy");
	}

	private static void process(String language, String pattern)
			throws InterruptedException, IOException {
		int minSize = 1000;
		int maxSize = 1050;
		long delay = 5000;
		WebDriver driver = new FirefoxDriver();
		String url = "https://github.com/search?ref=searchresults";
		url += "&type=Code";
		url += "&1=" + language;
		;
		url += "&q=\"" + pattern + "\"";
		//url += "+size:" + minSize + ".." + maxSize;
		for (int i = 1; i <= 100; i++) {
			fetchURL(driver, url + "&p=" + i);
			Thread.sleep(delay);
			printHits(driver, language);
		}
		driver.close();
	}
}
