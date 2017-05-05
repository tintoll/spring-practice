package or.kr.connect;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import or.kr.connect.domain.Book;
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
		
		Book book = dao.selectById(1);
		System.out.println(book);
		
		Book book2 = new Book("네이버 java","김강산", 512);
		Integer newId = dao.insert(book2);
		System.out.println(newId);
		System.out.println(dao.selectById(newId));
		
		//System.out.println(dao.deleteById(newId));
		//System.out.println(dao.countBooks());
		Book book3 = new Book("네이버 22java","김강222산", 2323);
		book3.setId(newId);
		dao.update(book3);
		System.out.println(dao.selectById(newId));
		
		
		context.close();
	}
}
