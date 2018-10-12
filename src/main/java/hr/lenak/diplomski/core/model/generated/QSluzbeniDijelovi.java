package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;


/**
 * QSluzbeniDijelovi is a Querydsl query type for SluzbeniDijelovi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSluzbeniDijelovi extends EntityPathBase<SluzbeniDijelovi> {

    private static final long serialVersionUID = 759101911L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSluzbeniDijelovi sluzbeniDijelovi = new QSluzbeniDijelovi("sluzbeniDijelovi");

    public final StringPath donositelj = createString("donositelj");

    public final StringPath fileName = createString("fileName");

    public final StringPath kljucneRijeci = createString("kljucneRijeci");

    public final QNarodneNovine narodneNovine;

    public final StringPath naslov = createString("naslov");

    public final StringPath potpisnik = createString("potpisnik");

    public final NumberPath<Long> sdoId = createNumber("sdoId", Long.class);

    public final StringPath sortIndex = createString("sortIndex");

    public final NumberPath<java.math.BigDecimal> stranica = createNumber("stranica", java.math.BigDecimal.class);

    public final ListPath<TekstoviSluzbeni, QTekstoviSluzbeni> tekstoviSluzbenis = this.<TekstoviSluzbeni, QTekstoviSluzbeni>createList("tekstoviSluzbenis", TekstoviSluzbeni.class, QTekstoviSluzbeni.class, PathInits.DIRECT2);

    public final QTipoviDokumenata tipDokumenta;

    public QSluzbeniDijelovi(String variable) {
        this(SluzbeniDijelovi.class, forVariable(variable), INITS);
    }

    public QSluzbeniDijelovi(Path<? extends SluzbeniDijelovi> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSluzbeniDijelovi(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSluzbeniDijelovi(PathMetadata metadata, PathInits inits) {
        this(SluzbeniDijelovi.class, metadata, inits);
    }

    public QSluzbeniDijelovi(Class<? extends SluzbeniDijelovi> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.narodneNovine = inits.isInitialized("narodneNovine") ? new QNarodneNovine(forProperty("narodneNovine")) : null;
        this.tipDokumenta = inits.isInitialized("tipDokumenta") ? new QTipoviDokumenata(forProperty("tipDokumenta")) : null;
    }

}

