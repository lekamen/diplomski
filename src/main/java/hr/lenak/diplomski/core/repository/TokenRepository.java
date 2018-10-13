package hr.lenak.diplomski.core.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import hr.lenak.diplomski.core.model.Token;

@Repository
public class TokenRepository extends QueryDslRepository<Token, Long> {

	public void createListaTokena(List<Token> tokeni) {
		for (Token t : tokeni) {
			this.create(t);
		}
	}
}
