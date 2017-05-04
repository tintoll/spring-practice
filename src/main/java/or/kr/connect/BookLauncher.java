package or.kr.connect;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import or.kr.connect.persistence.BookDao;

public class BookLauncher {
	public static void main(String[] args) throws Exception {
		// 웹 콘솔에서 접속중이면  컨넥션에러가 나옴 
		//DriverManagerDataSource dataSource = new DriverManagerDataSource();
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//DataSource dataSource = context.getBean(DataSource.class);
		
		BookDao dao = context.getBean(BookDao.class); // new BookDao(dataSource);
		int count = dao.countBooks();
		System.out.println(count);
		
		context.close();
	}
}
