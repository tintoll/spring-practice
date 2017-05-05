package or.kr.connect.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import or.kr.connect.domain.Book;

@Repository
public class BookDao {
	
	private NamedParameterJdbcTemplate jdbc;
	private SimpleJdbcInsert insertAction;
	
	public BookDao(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
		this.insertAction = new SimpleJdbcInsert(dataSource)
							.withTableName("book")			 // 테이블
							.usingGeneratedKeyColumns("id"); // 자동으로 생성되는 키 컬
	}
	
	private static final String COUNT_BOOK = "SELECT COUNT(*) FROM book";
	private static final String SELECT_BY_ID = "SELECT id, title, author, pages FROM book WHERE id = :id";
	
	public int countBooks() {
		Map<String, Object> params = Collections.emptyMap();
		return jdbc.queryForObject(COUNT_BOOK, params, Integer.class);
	}
	
	// BeanPropertyRowMapper.newInstance()로 생성한 객체는 멀티스레드에서 접근해도 안전하기 때문에 아래와 같이 멤버 변수로 선언하고 바로 초기화를 해도 무방하다.
	private RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
	 
	public Book selectById(Integer id) {
		/*
		RowMapper<Book> rowMapper = (rs, i) -> {
			Book book = new Book();
			book.setId(rs.getInt("id"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			book.setPages((Integer)rs.getObject("pages"));
			return book;
		};
		*/
		// BeanPropertyRowMapper 활용
		// RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
		
		Map<String , Object> params = new HashMap<>();
		params.put("id", id);
		return jdbc.queryForObject(SELECT_BY_ID, params, rowMapper);
	}
	
	// 실무에서는 관례적으로 DB에 저장되는 값은 원시타입인 int대신 java.lang.Integer를 쓰는 경우가 많다. 
	public Integer insert(Book book) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(book);
		return insertAction.executeAndReturnKey(params).intValue();
	}
	
	
	static final String DELETE_BY_ID = "DELETE FROM book WHERE id = :id";
	public int deleteById(Integer id) {
		Map<String, ?> params = Collections.singletonMap("id", id);
		return jdbc.update(DELETE_BY_ID, params);
	}
	
	private static final String UPDATE = "UPDATE book SET title = :title , author = :author, pages = :pages WHERE id = :id";
	public int update(Book book) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(book);
		return jdbc.update(UPDATE, params);
	}
}
