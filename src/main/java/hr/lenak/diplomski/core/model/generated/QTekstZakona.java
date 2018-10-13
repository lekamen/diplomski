package hr.lenak.diplomski.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTekstZakona is a Querydsl query type for TekstZakona
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTekstZakona extends EntityPathBase<TekstZakona> {

    private static final long serialVersionUID = -204769868L;

    public static final QTekstZakona tekstZakona = new QTekstZakona("tekstZakona");

    public final NumberPath<Integer> brojFilea = createNumber("brojFilea", Integer.class);

    public final NumberPath<Long> tekstZakonaId = createNumber("tekstZakonaId", Long.class);

    public final ListPath<Token, QToken> tokens = this.<Token, QToken>createList("tokens", Token.class, QToken.class, PathInits.DIRECT2);

    public final NumberPath<Long> tsiId = createNumber("tsiId", Long.class);

    public QTekstZakona(String variable) {
        super(TekstZakona.class, forVariable(variable));
    }

    public QTekstZakona(Path<? extends TekstZakona> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTekstZakona(PathMetadata metadata) {
        super(TekstZakona.class, metadata);
    }

}

