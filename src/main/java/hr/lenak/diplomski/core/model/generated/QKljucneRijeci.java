package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import hr.lenak.diplomski.core.model.KljucneRijeci;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QKljucneRijeci is a Querydsl query type for KljucneRijeci
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QKljucneRijeci extends EntityPathBase<KljucneRijeci> {

    private static final long serialVersionUID = 1129904129L;

    public static final QKljucneRijeci kljucneRijeci = new QKljucneRijeci("kljucneRijeci");

    public final NumberPath<Integer> brojFilea = createNumber("brojFilea", Integer.class);

    public final NumberPath<Long> kljucneRijeciId = createNumber("kljucneRijeciId", Long.class);

    public final StringPath kwTextrank = createString("kwTextrank");

    public final StringPath kwTextrankMulWinSize = createString("kwTextrankMulWinSize");

    public final StringPath kwTfidf = createString("kwTfidf");
    
    public final StringPath kwTextrankIdf = createString("kwTextrankIdf");

    public final NumberPath<Long> tekstZakonaId = createNumber("tekstZakonaId", Long.class);

    public final NumberPath<Long> tsiId = createNumber("tsiId", Long.class);

    public QKljucneRijeci(String variable) {
        super(KljucneRijeci.class, forVariable(variable));
    }

    public QKljucneRijeci(Path<? extends KljucneRijeci> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKljucneRijeci(PathMetadata metadata) {
        super(KljucneRijeci.class, metadata);
    }

}

