package orm;

import java.io.IOException;
import java.util.List;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.File;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.GHCommit.ShortInfo;

public class Test {
	public static void main(String[] args) throws IOException {
		GitHub github = GitHub.connect();
		GHRepository r = github.getRepository("anthem001/NHibernateDemo");
		System.out.println(r.listCollaborators().asList().size());
		System.out.println(r.listCommits().asList().size());
		System.out.println(r.getForks());
		System.out.println(r.getLanguage());
		System.out.println(r.getSize());

		PagedIterable<GHCommit> commits = r.listCommits();
		for (GHCommit commit : commits) {
			if (commit != null) {
				System.out.println("Commit is not null");
				
				//System.out.println(commit.getLinesAdded());
				//System.out.println(commit.getLinesChanged());
				//System.out.println(commit.getLinesDeleted());
				System.out.println(commit.getSHA1());
				System.out.println(commit.getAuthor());
				ShortInfo info = commit.getCommitShortInfo();
				System.out.println("Comment Count "+info.getCommentCount());
				System.out.println("Message" + info.getMessage());
				System.out.println("Author "+info.getAuthor().getName()+" "+info.getAuthor().getEmail()+" "+info.getAuthor().getDate());
				System.out.println("Committer "+info.getCommitter().getName()+" "+info.getCommitter().getEmail()+" "+info.getCommitter().getDate());
				System.out.println(commit.getSHA1());
				String parentShas = "";
				for(String p:commit.getParentSHA1s())
					parentShas+=p+",";
				System.out.println(parentShas.substring(0,parentShas.length()));
				List<File> files = r.getCommit(commit.getSHA1()).getFiles();
				System.out.println("Files Size "+files.size());
				for (File file : files) {
					System.out.println(file.getFileName());
					System.out.println(file.getLinesAdded());
					System.out.println(file.getLinesChanged());
					System.out.println(file.getLinesDeleted());
					//System.out.println(file.getPatch());
				}

			}
		}

	}
}
