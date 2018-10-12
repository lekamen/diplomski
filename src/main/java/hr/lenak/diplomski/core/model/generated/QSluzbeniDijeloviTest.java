package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import hr.lenak.diplomski.core.model.SluzbeniDijeloviTest;


/**
 * QSluzbeniDijeloviTest is a Querydsl query type for SluzbeniDijeloviTest
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSluzbeniDijeloviTest extends EntityPathBase<SluzbeniDijeloviTest> {

    private static final long serialVersionUID = 521662217L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSluzbeniDijeloviTest sluzbeniDijeloviTest = new QSluzbeniDijeloviTest("sluzbeniDijeloviTest");

    public final StringPath donositelj = createString("donositelj");

    public final StringPath fileName = createString("fileName");

    public final StringPath kljucneRijeci = createString("kljucneRijeci");

    public final QNarodneNovine narodneNovine;

    public final StringPath naslov = createString("naslov");

    public final StringPath potpisnik = createString("potpisnik");

    public final NumberPath<Long> sdoId = createNumber("sdoId", Long.class);

    public final StringPath sortIndex = createString("sortIndex");

    public final NumberPath<java.math.BigDecimal> stranica = createNumber("stranica", java.math.BigDecimal.class);

    public final QTipoviDokumenata tipDokumenta;

    public QSluzbeniDijeloviTest(String variable) {
        this(SluzbeniDijeloviTest.class, forVariable(variable), INITS);
    }

    public QSluzbeniDijeloviTest(Path<? extends SluzbeniDijeloviTest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSluzbeniDijeloviTest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSluzbeniDijeloviTest(PathMetadata metadata, PathInits inits) {
        this(SluzbeniDijeloviTest.class, metadata, inits);
    }

    public QSluzbeniDijeloviTest(Class<? extends SluzbeniDijeloviTest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.narodneNovine = inits.isInitialized("narodneNovine") ? new QNarodneNovine(forProperty("narodneNovine")) : null;
        this.tipDokumenta = inits.isInitialized("tipDokumenta") ? new QTipoviDokumenata(forProperty("tipDokumenta")) : null;
    }

}

