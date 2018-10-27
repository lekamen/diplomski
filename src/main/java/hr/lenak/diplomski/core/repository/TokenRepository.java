package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QToken.token;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;

@Repository
public class TokenRepository extends QueryDslRepository<Token, Long> {

	@Transactional
	public void createListaTokena(List<Token> tokeni) {
		for (Token t : tokeni) {
			this.create(t);
		}
	}
	
	@Transactional
	public List<Token> findByTekstZakona(TekstZakona tekst) {
		return select(token)
			.from(token)
			.where(token.tekstZakona.eq(tekst))
			.orderBy(token.position.asc())
			.fetch();
	}
}
