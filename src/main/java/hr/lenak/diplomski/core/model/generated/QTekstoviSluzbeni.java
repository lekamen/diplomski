package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import hr.lenak.diplomski.core.model.TekstoviSluzbeni;


/**
 * QTekstoviSluzbeni is a Querydsl query type for TekstoviSluzbeni
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTekstoviSluzbeni extends EntityPathBase<TekstoviSluzbeni> {

    private static final long serialVersionUID = 135717992L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTekstoviSluzbeni tekstoviSluzbeni = new QTekstoviSluzbeni("tekstoviSluzbeni");

    public final StringPath chrset = createString("chrset");

    public final StringPath ctxprivate = createString("ctxprivate");

    public final StringPath euLink = createString("euLink");

    public final QSluzbeniDijelovi sluzbeniDijelovi;

    public final NumberPath<java.math.BigDecimal> sort = createNumber("sort", java.math.BigDecimal.class);

    public final ArrayPath<byte[], Byte> tekst = createArray("tekst", byte[].class);

    public final NumberPath<Long> tsiId = createNumber("tsiId", Long.class);

    public QTekstoviSluzbeni(String variable) {
        this(TekstoviSluzbeni.class, forVariable(variable), INITS);
    }

    public QTekstoviSluzbeni(Path<? extends TekstoviSluzbeni> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTekstoviSluzbeni(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTekstoviSluzbeni(PathMetadata metadata, PathInits inits) {
        this(TekstoviSluzbeni.class, metadata, inits);
    }

    public QTekstoviSluzbeni(Class<? extends TekstoviSluzbeni> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sluzbeniDijelovi = inits.isInitialized("sluzbeniDijelovi") ? new QSluzbeniDijelovi(forProperty("sluzbeniDijelovi"), inits.get("sluzbeniDijelovi")) : null;
    }

}

