package orm;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Crawler {

	private static void printHits(WebDriver driver, String language) {
		String xpath = "//div[@class='code-list']/*";
		List<WebElement> hits = driver.findElements(By.xpath(xpath));
		Iterator<WebElement> iter = hits.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getText());
			xpath = "//p[@class='title']/a";
			List<WebElement> links = (List<WebElement>) driver.findElements(By
					.xpath(xpath));
			String repositoryURL = links.get(0).getAttribute("href");
			String fileURL = links.get(1).getAttribute("href");
			System.out.println(repositoryURL + " " + fileURL);
			saveDetail(repositoryURL, fileURL, language);

		}
	}

	private static void saveDetail(String repositoryURL, String fileURL,
			String language) {
		Session session = null;
		GithubProjects bean = null;
		switch (language) {
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

	public static void main(String[] args) throws InterruptedException {
		process("java", "import javax.persistence.Entity");
		process("csharp", "nhibernate");
		process("php", "use Doctrine");
		process("python","from sqlalchemy");
	}

	private static void process(String language, String pattern)
			throws InterruptedException {
		int minSize = 1000;
		int maxSize = 1050;
		long delay = 5000;
		WebDriver driver = new FirefoxDriver();
		String url = "https://github.com/search?ref=searchresults";
		url += "&type=Code";
		url += "&1=" + language;
		;
		url += "&q=\"" + pattern + "\"";
		url += "+size:" + minSize + ".." + maxSize;
		for (int i = 1; i <= 100; i++) {
			fetchURL(driver, url + "&p=" + i);
			Thread.sleep(delay);
			printHits(driver, language);
		}
		driver.close();
	}
}
