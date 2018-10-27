package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import hr.lenak.diplomski.core.model.SvePojaveRijeci;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSvePojaveRijeci is a Querydsl query type for SvePojaveRijeci
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSvePojaveRijeci extends EntityPathBase<SvePojaveRijeci> {

    private static final long serialVersionUID = 622699706L;

    public static final QSvePojaveRijeci svePojaveRijeci = new QSvePojaveRijeci("svePojaveRijeci");

    public final NumberPath<Long> sprId = createNumber("sprId", Long.class);

    public final NumberPath<Long> tokenIdDrugi = createNumber("tokenIdDrugi", Long.class);

    public final NumberPath<Long> tokenIdPrvi = createNumber("tokenIdPrvi", Long.class);

    public QSvePojaveRijeci(String variable) {
        super(SvePojaveRijeci.class, forVariable(variable));
    }

    public QSvePojaveRijeci(Path<? extends SvePojaveRijeci> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSvePojaveRijeci(PathMetadata metadata) {
        super(SvePojaveRijeci.class, metadata);
    }

}

