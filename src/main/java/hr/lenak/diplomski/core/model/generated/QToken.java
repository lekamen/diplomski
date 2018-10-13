package hr.lenak.diplomski.core.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QToken extends EntityPathBase<Token> {

    private static final long serialVersionUID = -1502890412L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToken token = new QToken("token");

    public final EnumPath<hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum> kategorija = createEnum("kategorija", hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.class);

    public final StringPath lemma = createString("lemma");

    public final StringPath tag = createString("tag");

    public final QTekstZakona tekstZakona;

    public final NumberPath<Long> tokenId = createNumber("tokenId", Long.class);

    public final StringPath value = createString("value");

    public QToken(String variable) {
        this(Token.class, forVariable(variable), INITS);
    }

    public QToken(Path<? extends Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToken(PathMetadata metadata, PathInits inits) {
        this(Token.class, metadata, inits);
    }

    public QToken(Class<? extends Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tekstZakona = inits.isInitialized("tekstZakona") ? new QTekstZakona(forProperty("tekstZakona")) : null;
    }

}

