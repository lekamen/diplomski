package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.core.model.SluzbeniDijelovi;


/**
 * QNarodneNovine is a Querydsl query type for NarodneNovine
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNarodneNovine extends EntityPathBase<NarodneNovine> {

    private static final long serialVersionUID = 897056273L;

    public static final QNarodneNovine narodneNovine = new QNarodneNovine("narodneNovine");

    public final NumberPath<java.math.BigDecimal> broj = createNumber("broj", java.math.BigDecimal.class);

    public final DateTimePath<java.time.LocalDateTime> datumIzdanja = createDateTime("datumIzdanja", java.time.LocalDateTime.class);

    public final NumberPath<Long> nnId = createNumber("nnId", Long.class);

    public final ListPath<SluzbeniDijelovi, QSluzbeniDijelovi> sluzbeniDijelovis = this.<SluzbeniDijelovi, QSluzbeniDijelovi>createList("sluzbeniDijelovis", SluzbeniDijelovi.class, QSluzbeniDijelovi.class, PathInits.DIRECT2);

    public QNarodneNovine(String variable) {
        super(NarodneNovine.class, forVariable(variable));
    }

    public QNarodneNovine(Path<? extends NarodneNovine> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNarodneNovine(PathMetadata metadata) {
        super(NarodneNovine.class, metadata);
    }

}

